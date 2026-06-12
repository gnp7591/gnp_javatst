# Agents Framework - Selenium Test Automation with n8n Integration

## 📋 Document Overview

This document defines the autonomous agents and integration workflows that enable intelligent test execution, automated reporting, and multi-channel stakeholder notifications for the Seleniumtest framework. It establishes the operational model for test automation as a scalable, event-driven system.

---

## 🎯 Strategic Goals

| Goal | Agent Owner | Success Metric |
|------|-------------|----------------|
| **Reliable, Scalable Test Execution** | Test Executor | 100% suite completion rate; sub-2-minute smoke test cycles |
| **Actionable Insights via Email** | Report Analyzer | <5min report generation; 100% requirement traceability |
| **Developer Visibility** | Report Analyzer → Gmail | Real-time delivery to gnpuranik479@gmail.com; instant failure alerts |
| **Developer Productivity** | Test Executor + Report Analyzer | <1min test failure root-cause identification via detailed logs/screenshots |
| **Continuous Integration Readiness** | Test Executor | Parallel execution across 3+ browsers with <15min total CI cycle |

---

## 👥 Agent Definitions

### **Agent 1: Test Executor**

#### Mission
Orchestrate deterministic test suite execution across multiple environments (local, CI/CD, cloud browsers) and collect standardized Allure test results for downstream processing.

#### Ownership Scope
- Invoke TestNG suites via Maven (testng-smoke.xml, testng-grouped.xml, testng-all-uitests.xml)
- Manage WebDriver provisioning (local Chrome/Firefox/Safari, BrowserStack, Sauce Labs)
- Execute parallel test jobs across browser matrix
- Collect Allure result JSON artifacts from `target/allure-results/`
- Publish raw test artifacts to artifact storage (local filesystem, S3, Azure Blob, etc.)
- Report execution status (success/failure/timeout) to n8n orchestrator

#### Quality Bars
- **Reliability**: No flaky retries without explicit configuration; same test run twice = same result
- **Performance**: Smoke test suite < 2 minutes; full suite < 15 minutes on single machine
- **Observability**: All test output captured; driver logs, network logs, and screenshots archived with each run
- **Idempotency**: Same test run on same environment produces identical results

#### Core Principle (Boundary)
Test Executor **does not** interpret test results, generate reports, or make notifications. It only executes and collects. Report processing and stakeholder communication belong to Report Analyzer.

#### Communication Rules
- **Input**: Receives execution request from n8n workflow with parameters:
  - `suiteFile` (e.g., "testng-smoke.xml")
  - `browserMatrix` (e.g., ["chrome", "firefox", "safari"])
  - `environment` (e.g., "local", "ci", "cloud")
  - `parallelCount` (e.g., 3 = 3 concurrent browser instances)
- **Output**: Returns structured execution report:
  ```json
  {
    "executionId": "exec-20260612-143022",
    "suite": "testng-smoke.xml",
    "startTime": "2026-06-12T14:30:22Z",
    "endTime": "2026-06-12T14:32:45Z",
    "status": "PASSED|FAILED|TIMEOUT",
    "totalTests": 6,
    "passed": 6,
    "failed": 0,
    "skipped": 0,
    "allureResultsPath": "/target/allure-results/",
    "artifactStorageUrl": "s3://bucket/exec-20260612-143022/",
    "browserMatrix": ["chrome", "firefox"],
    "environment": "ci"
  }
  ```
- **Failure Escalation**: If execution fails, includes error log excerpt and recommendation for retry or manual investigation

---

### **Agent 2: Report Analyzer**

#### Mission
Transform raw Allure test results into actionable insights for multiple audience types (developers, QA leads, executives) and orchestrate multi-channel notifications via Slack, Teams, and Email.

#### Ownership Scope
- Parse Allure result JSON artifacts and generate metrics (pass rate %, flaky test count, duration breakdown)
- Identify trending patterns (regressions, emerging flaky tests, performance degradation)
- Produce audience-specific report formats:
  - **Developer Report**: Detailed failures with stack traces, screenshots, video links, affected components
  - **Executive Summary**: Pass/fail rates, trend indicators (↑ ↓ =), estimated cost/ROI impact
  - **Metrics Dashboard**: Historical trend data (30-day rolling window), SLA compliance %
- Query test history database to detect new vs. recurring failures
- Orchestrate notifications to Gmail (gnpuranik479@gmail.com)
- Store results in persistent database for trend analysis and historical reporting
- Generate Allure HTML report and publish to accessible URL (for embedding in emails/Slack)

#### Quality Bars
- **Accuracy**: All metrics derived from Allure JSON; 100% requirement traceability to test cases
- **Timeliness**: Report generation completes <5 minutes after test execution ends
- **Completeness**: All failures include root cause hypothesis and reproduction steps
- **Audience Fit**: Dev reports include code/stack traces; Executive reports omit technical jargon

#### Core Principle (Boundary)
Report Analyzer **does not** execute tests or modify test configuration. It only consumes execution results and produces intelligence. Test execution requests come from n8n orchestrator (not Report Analyzer itself).

#### Communication Rules
- **Input**: Receives execution report from Test Executor and historical baseline from database:
  - Allure results path
  - Execution metadata (suite, environment, browser matrix, duration)
  - Previous 30-day test history (for trend detection)
- **Output**: Generates multi-format report package:
  ```json
  {
    "reportId": "report-20260612-143500",
    "executionId": "exec-20260612-143022",
    "generatedAt": "2026-06-12T14:35:00Z",
    "summary": {
      "totalTests": 6,
      "passed": 6,
      "failed": 0,
      "passRate": 100.0,
      "duration": 165,
      "trend": "stable"
    },
    "developerReport": {
      "format": "markdown",
      "url": "s3://reports/report-20260612-143500/developer-summary.md",
      "highlights": ["All tests passed", "No new failures detected"]
    },
    "executiveReport": {
      "format": "html",
      "url": "s3://reports/report-20260612-143500/executive-dashboard.html",
      "metrics": {"passRate": "100%", "trend": "↑ +5% vs 7-day avg"}
    },
    "allureUrl": "https://reports.internal.com/allure-report/20260612-143022/index.html",
    "notificationStatus": {
      "slack": "SENT",
      "teams": "SENT",
      "email": "SENT"
    }
  }
  ```
- **Notification Routing**: 
  - If `passRate >= 95%`: Standard summary to Slack + Teams
  - If `passRate < 95%` AND `failed <= 3`: Detailed dev report to Slack + email to QA team
  - If `passRate < 90%` OR `criticalTests failed`: Escalation alert + page-on-call (future: PagerDuty integration)

---

## 🔄 n8n Workflow Definitions

### **Workflow 1: Scheduled Test Execution**

**Trigger**: Cron schedule (configurable)  
**Schedule Options** (set at deployment):
- `0 8 * * MON-FRI` — Daily at 8 AM (weekdays only)
- `0 */6 * * *` — Every 6 hours
- `0 22 * * *` — Nightly smoke tests
- Webhook-triggered (on-demand via REST API)

**Workflow Steps**:

```
[Cron Trigger] 
    ↓
[Load Configuration] → Fetch env vars: SUITE_FILE, BROWSER_MATRIX, PARALLEL_COUNT
    ↓
[Validate Inputs] → Ensure SUITE_FILE exists, BROWSER_MATRIX is non-empty
    ↓
[Invoke Test Executor] → Call local Maven command OR remote webhook
    │ Command (local): mvn clean test -DsuiteXmlFile="${SUITE_FILE}"
    │ Webhook (remote): POST /api/test-executor/run with JSON body
    ↓
[Poll for Completion] → Every 10 sec until status != RUNNING (max 20 min timeout)
    ↓
[Check Execution Result] → Extract executionId, status, allureResultsPath
    ↓
[Invoke Report Analyzer] → Pass executionId + allureResultsPath + historical baseline
    ↓
[Wait for Report] → Poll until reportId returns (max 5 min timeout)
    ↓
[Route Notifications] → Based on passRate, send appropriate alerts
    ↓
[Log Completion] → Store workflow execution log to database for audit trail
```

**Configuration Variables** (stored in n8n environment):
```yaml
SUITE_FILE: "testng-smoke.xml"        # Options: testng-smoke.xml, testng-grouped.xml, testng-all-uitests.xml
BROWSER_MATRIX: "chrome,firefox"      # CSV; options: chrome, firefox, safari, edge
PARALLEL_COUNT: 2                      # Concurrent browser instances (1-5 recommended)
ENVIRONMENT: "ci"                      # Options: local, ci, cloud
TEST_TIMEOUT_MINUTES: 15
REPORT_TIMEOUT_MINUTES: 5
NOTIFICATION_EMAIL: "gnpuranik479@gmail.com"
```

**Error Handling**:
- If Test Executor times out → Retry once with PARALLEL_COUNT = 1
- If retry fails → Send escalation alert to on-call QA lead
- If Report Analyzer fails → Send partial report (raw Allure link) to stakeholders
- All errors logged with context (environment, suite, browser) for debugging

---

### **Workflow 2: Multi-Channel Result Notifications**

**Trigger**: Report Analyzer completion (input from Workflow 1)

**Workflow Steps**:

```
[Receive Report Package from Analyzer]
    ↓
[Decision: passRate >= 95%?]
    ├─ YES → [Standard Success Notification] 
    │         ├─ Send Gmail to dev-team (brief summary + link to Allure)
    │         ├─ POST to Teams QA Reports (metric card: Pass %, Duration)
    │         └─ Send Email to distribution list (HTML summary + Allure URL)
    │
    └─ NO → [Detailed Failure Notification]
             ├─ Send Gmail to dev-team (thread with failed test details, screenshots)
             ├─ POST to Teams QA Reports (red alert card + top 3 failures)
             ├─ Send Email to qa-team@company.com (detailed report + next steps)
             └─ [Decision: passRate < 90%?]
                 └─ YES → Send SMS alert to on-call QA lead + create incident ticket
```

**Gmail Notification Template** (Success):
```
To: gnpuranik479@gmail.com
Subject: ✅ Test Automation Report | 6/6 Passed (100%)

📊 **Execution Summary**
- Result: 6/6 Passed (100%)
- Duration: 2m 45s
- Browsers: Chrome, Firefox
- Suite: testng-smoke.xml
- Timestamp: 2026-06-12T14:32:45Z

📈 **Trend**: ↑ +5% vs 7-day average

🔗 [View Complete Allure Report](https://reports.internal.com/allure/20260612-143022/index.html)

All tests passing successfully! No action needed.
```

**Gmail Notification Template** (Failure):
```
To: gnpuranik479@gmail.com
Subject: ❌ Test Automation Report | 4/6 Passed (67%) - ATTENTION REQUIRED

⚠️ **CRITICAL** - Test Suite Degradation Detected

📊 **Execution Summary**
- Result: 4/6 Passed (67%)
- Duration: 3m 12s
- Browsers: Chrome ❌, Firefox ✅
- Suite: testng-smoke.xml
- Timestamp: 2026-06-12T14:35:22Z

❌ **Failed Tests** (2 total):
  1️⃣ testAddProductToCart (Chrome)
     Error: Timeout after 5s waiting for element
     Screenshot: [Attached]
     Log: [Attached]
  
  2️⃣ testCheckout (Chrome)
     Error: StaleElementException on payment button
     Screenshot: [Attached]
     Log: [Attached]

🔍 **Root Cause Analysis**:
- Chrome WebDriver compatibility issue detected
- Possible timing issue on Cart page load
- Consider updating WebDriver version

📈 **Historical Trend**:
- Pass rate dropped from 100% (yesterday) to 67% (today)
- First occurrence of Chrome-specific failures

🔗 [View Complete Allure Report](https://reports.internal.com/allure/20260612-143022/index.html)
🔗 [View Maven Build Logs](https://ci.internal.com/jobs/selenium-tests/20260612-143022/logs)

**Recommended Actions**:
1. Review Chrome WebDriver version compatibility
2. Investigate timeout on Cart page load
3. Check for recent code changes affecting navigation
4. Run diagnostics on Cart component
```

---

### **Workflow 3: Result Storage & Metrics Database**

**Trigger**: Report Analyzer completion (parallel to Workflow 2)

**Workflow Steps**:

```
[Receive Report Package]
    ↓
[Extract Metrics] → Pass rate, test count, duration, failed test names, browser matrix
    ↓
[Query Historical Baseline] → Last 30 days of test results from database
    ↓
[Detect Trends] → 
    ├─ New failures (first occurrence in past 30 days)
    ├─ Recurring failures (appeared >3 times in past 30 days)
    ├─ Flaky tests (pass rate 50-95% over past 30 days)
    └─ Performance degradation (execution duration trend ↑)
    ↓
[Classify Test Results] → For each failed test:
    ├─ Root cause bucket (Infrastructure, Product Code, Test Code, External Service)
    ├─ Affected component (Cart, Checkout, Navigation, etc.)
    └─ Criticality (Blocker, High, Medium, Low)
    ↓
[Store to Database] → Insert execution record + metrics + classification
    ↓
[Publish to Metrics Dashboard] → Update Grafana/BI dashboards in real-time
    ↓
[Archive Artifacts] → 
    ├─ Allure HTML report → Azure Blob / S3 / local filesystem
    ├─ Screenshots → Versioned storage (for trend comparison)
    └─ Logs → Compressed archive (searchable index for future replay)
    ↓
[Generate 7-Day & 30-Day Summary] → Aggregate metrics for reporting
```

**Database Schema** (example: PostgreSQL):
```sql
CREATE TABLE test_executions (
  id SERIAL PRIMARY KEY,
  execution_id VARCHAR(50) UNIQUE,
  report_id VARCHAR(50),
  suite_name VARCHAR(100),
  environment VARCHAR(20),
  browser_matrix TEXT,
  started_at TIMESTAMP,
  completed_at TIMESTAMP,
  total_tests INT,
  passed_tests INT,
  failed_tests INT,
  skipped_tests INT,
  pass_rate DECIMAL(5,2),
  duration_seconds INT,
  allure_url TEXT,
  artifacts_path TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE failed_tests (
  id SERIAL PRIMARY KEY,
  execution_id VARCHAR(50),
  test_name VARCHAR(255),
  failure_reason TEXT,
  root_cause_bucket VARCHAR(50),
  affected_component VARCHAR(100),
  criticality VARCHAR(20),
  screenshot_url TEXT,
  log_excerpt TEXT,
  is_new_failure BOOLEAN,
  is_flaky BOOLEAN
);

CREATE TABLE metrics_trends (
  id SERIAL PRIMARY KEY,
  date_trunc DATE,
  environment VARCHAR(20),
  suite_name VARCHAR(100),
  avg_pass_rate DECIMAL(5,2),
  avg_duration_seconds INT,
  new_failures_count INT,
  flaky_tests_count INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Metrics Dashboard** (Grafana/BI Platform):
- **Real-time pane**: Latest execution status, pass rate gauge, test count histogram
- **Trend pane**: 30-day pass rate line chart, duration trend, flaky test count over time
- **Failure analysis**: Top 10 most common failure root causes (pie chart), affected components (bar chart)
- **Browser comparison**: Pass rate by browser (multi-series line chart)
- **SLA tracking**: % of test runs completing within target duration (e.g., 95% within 10 min)

---

## 🏗️ Integration Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         n8n Orchestration Layer                      │
└─────────────────────────────────────────────────────────────────────┘
                  │
         ┌────────┼────────┐
         ↓        ↓        ↓
    [Cron]   [Webhook]  [Manual]
         └────────┼────────┘
                  ↓
    ┌─────────────────────────┐
    │  Workflow 1: Execution  │
    │  (validate, schedule)   │
    └────────────┬────────────┘
                 ↓
         ┌──────────────────┐
         │  Test Executor   │  ← Invokes Maven + TestNG + WebDriver
         │  (runs tests)    │
         └────────┬─────────┘
                  ↓
         ┌──────────────────────┐
         │ Allure Results JSON  │
         │ + Execution Report   │
         └────────┬─────────────┘
                  ↓
    ┌─────────────────────────────────┐
    │  Report Analyzer                │ ← Consumes Allure JSON
    │  (analyzes + generates reports) │
    └────────────┬────────────────────┘
                 ↓
        ┌────────┴────────┐
        ↓                 ↓
    [Workflow 2]     [Workflow 3]
    Gmail Notification Storage &
    gnpuranik479@    Metrics
    gmail.com        (Database)
        │                 │
        ├─→ Gmail ←───────┤
        └─→ Metrics Dashboard
```

---

## 🔧 Configuration & Deployment

### **Environment Setup**

1. **n8n Installation**
   ```bash
   # Option A: Docker
   docker run -d \
     -p 5678:5678 \
     -v ~/.n8n:/home/node/.n8n \
     n8nio/n8n

   # Option B: npm
   npm install -g n8n
   n8n start
   ```

2. **n8n Credentials** (configure in UI or via env vars):
   ```yaml
   GMAIL_SMTP_SERVER: "smtp.gmail.com"
   GMAIL_SMTP_PORT: 587
   GMAIL_USERNAME: "gnpuranik479@gmail.com"
   GMAIL_APP_PASSWORD: "${VAULT_GMAIL_APP_PASSWORD}"  # Use Google App Password for security
   DATABASE_URL: "postgresql://user:pass@db.internal.com:5432/test_metrics"
   AWS_ACCESS_KEY_ID: "${VAULT_AWS_KEY}"
   AWS_SECRET_ACCESS_KEY: "${VAULT_AWS_SECRET}"
   ```

3. **Test Executor Configuration** (Maven properties):
   ```properties
   # .env or environment variables for Maven invocation
   TEST_SUITE_FILE=testng-smoke.xml
   TEST_BROWSERS=chrome,firefox,safari
   TEST_PARALLEL_COUNT=3
   WEBDRIVER_MANAGER_ENABLED=true
   ALLURE_RESULTS_PATH=target/allure-results
   ```

4. **Report Analyzer Configuration** (n8n environment):
   ```yaml
   ALLURE_RESULTS_PARSER: "python"  # Parser script location
   TREND_WINDOW_DAYS: 30
   FLAKY_THRESHOLD_PERCENT: 50      # Tests with 50-95% pass rate = flaky
   FAILURE_CLASSIFICATION_MODEL: "heuristic"  # or "ml" for future
   METRICS_DB_RETENTION_DAYS: 90
   ```

### **Deployment Checklist**

- [ ] n8n instance running and accessible
- [ ] Gmail account (gnpuranik479@gmail.com) configured with App Password enabled (Settings → Security → 2-Step Verification → App passwords)
- [ ] Gmail SMTP credentials verified (test email send via n8n)
- [ ] PostgreSQL database provisioned and `test_executions` schema initialized
- [ ] Allure report storage location accessible (S3 bucket / Azure Blob / local NFS)
- [ ] Maven + Java 21 + WebDriver Manager configured on test execution machine(s)
- [ ] n8n workflows imported from `.n8n/workflows/*.json` (see next section)
- [ ] Cron schedule enabled and first test run validated
- [ ] NOTIFICATION_EMAIL environment variable set to gnpuranik479@gmail.com
- [ ] Monitoring alerts configured for n8n workflow failures

---

## 📦 Workflow Implementation Files

The following files define the n8n workflows:

| File | Purpose |
|------|---------|
| `.n8n/workflows/01-scheduled-execution.json` | Cron trigger + Test Executor invocation |
| `.n8n/workflows/02-multi-channel-notifications.json` | Result routing to Slack/Teams/Email |
| `.n8n/workflows/03-metrics-storage.json` | Database persistence + trend detection |
| `.n8n/credentials.yml` | Credentials config (Git-ignored, use env vars for secrets) |
| `.n8n/README.md` | Detailed deployment guide for n8n admin |

**To import workflows into n8n**:
1. Open n8n UI → Workflows → Import
2. Select `.n8n/workflows/01-scheduled-execution.json`
3. Verify webhook endpoints and credentials are configured
4. Click "Activate Workflow"
5. Repeat for workflows 02 and 03

---

## 🎯 Usage Examples

### **Example 1: Trigger Manual Test Run**

```bash
# Invoke Workflow 1 via webhook (e.g., from CI/CD pipeline)
curl -X POST "http://n8n.internal.com:5678/webhook/manual-test-trigger" \
  -H "Content-Type: application/json" \
  -d '{
    "suiteFile": "testng-smoke.xml",
    "browserMatrix": ["chrome"],
    "environment": "ci",
    "parallelCount": 1
  }'

# Response:
# {
#   "workflowExecutionId": "wf-exec-20260612-143022",
#   "status": "STARTED",
#   "estimatedDuration": 180
# }
```

### **Example 2: Query Test Metrics from Dashboard**

```sql
-- Get pass rate trend for last 7 days
SELECT 
  DATE_TRUNC('day', completed_at) as execution_date,
  ROUND(AVG(pass_rate), 2) as avg_pass_rate,
  COUNT(*) as executions_per_day
FROM test_executions
WHERE completed_at >= NOW() - INTERVAL '7 days'
GROUP BY DATE_TRUNC('day', completed_at)
ORDER BY execution_date ASC;

-- Output:
-- execution_date | avg_pass_rate | executions_per_day
-- 2026-06-06     | 100.00        | 2
-- 2026-06-07     | 95.50         | 2
-- 2026-06-08     | 92.33         | 3
-- 2026-06-09     | 98.00         | 2
-- 2026-06-10     | 96.50         | 2
-- 2026-06-11     | 94.00         | 2
-- 2026-06-12     | 100.00        | 1 (in progress)
```

### **Example 3: Investigate Flaky Test**

```bash
# Find flaky tests (pass rate 50-95%) from last 30 days
SELECT 
  test_name,
  COUNT(*) as executions,
  SUM(CASE WHEN is_new_failure = false THEN 1 ELSE 0 END) as failures,
  ROUND(100.0 * SUM(CASE WHEN is_new_failure = false THEN 1 ELSE 0 END) / COUNT(*), 2) as failure_rate
FROM failed_tests
WHERE created_at >= NOW() - INTERVAL '30 days'
GROUP BY test_name
HAVING ROUND(100.0 * SUM(CASE WHEN is_new_failure = false THEN 1 ELSE 0 END) / COUNT(*), 2) BETWEEN 5 AND 95
ORDER BY failure_rate DESC;

# Result: Identifies tests like "testCheckout" failing 60% of the time (flaky)
# QA lead can then investigate timing issues, race conditions, or environment instability
```

---

## 🚀 Future Enhancements

| Feature | Target Quarter | Owner |
|---------|-----------------|-------|
| **AI-Powered Root Cause Analysis** | Q3 2026 | Report Analyzer v2 |
| **Performance Trending & Regression Detection** | Q3 2026 | Metrics database + BI |
| **Self-Healing Test Retry Logic** | Q4 2026 | Test Executor v2 |
| **PagerDuty/Incident.io Integration** | Q2 2026 | Workflow 2 extension |
| **Distributed Execution** (multiple machines) | Q4 2026 | Test Executor scaling |
| **Mobile Browser Support** (via BrowserStack) | Q2 2026 | Test Executor v1.1 |
| **Test Impact Analysis** (which code changes affect which tests) | Q3 2026 | New agent: Code Analyzer |
| **Cost Optimization Dashboard** (test execution cost per browser/environment) | Q3 2026 | Metrics database |

---

## 📞 Support & Troubleshooting

### **Common Issues**

| Issue | Diagnosis | Solution |
|-------|-----------|----------|
| n8n workflow doesn't trigger on schedule | Cron expression syntax error or workflow not activated | Verify cron format in Workflow 1; click "Activate" button in n8n UI |
| Gmail notifications not received to gnpuranik479@gmail.com | SMTP credentials invalid or Gmail App Password not set | Verify GMAIL_USERNAME=gnpuranik479@gmail.com and GMAIL_APP_PASSWORD in n8n; test send via Gmail SMTP node; verify Google App Password enabled |
| Report Analyzer times out | Allure parsing slow or database query hanging | Check Allure results file size; review database query performance; increase timeout to 10 min |
| Test Executor fails with "WebDriver timeout" | Browser instance didn't start in time or environment resource-constrained | Reduce `PARALLEL_COUNT`; verify Chrome/Firefox/Safari installed; check system memory usage |
| Metrics not appearing in dashboard | Database connection failed or schema not initialized | Run schema SQL scripts; verify `DATABASE_URL` credential; check database logs |
| NOTIFICATION_EMAIL not configured | Environment variable missing | Set NOTIFICATION_EMAIL=gnpuranik479@gmail.com in n8n environment variables |

### **Debug Logs**

- **n8n logs**: View in n8n UI → Executions tab (filter by workflow); also `/home/node/.n8n/logs/`
- **Maven logs**: Captured in CI/CD pipeline logs; search for `[ERROR]` markers
- **Allure logs**: Check `target/allure-results/` for JSON parsing errors
- **Database logs**: PostgreSQL `pg_log/` directory or query slow logs

### **Contact & Escalation**

All test automation reports and alerts are sent directly to: **gnpuranik479@gmail.com**

- **Urgent Issues**: Check Gmail for CRITICAL FAILURE alerts (subject contains "CRITICAL")
- **Questions/Support**: Contact your n8n administrator

---

## 📚 Related Documentation

- [SETUP_GUIDE.md](SETUP_GUIDE.md) — Framework installation and first-time setup
- [QUICK_START_SUITES.md](QUICK_START_SUITES.md) — Quick reference for running test suites
- [TESTNG_SUITE_GUIDE.md](TESTNG_SUITE_GUIDE.md) — Detailed TestNG suite configuration
- [.n8n/README.md](.n8n/README.md) — n8n workflow deployment and troubleshooting
- Allure Documentation: https://docs.qameta.io/allure/
- n8n Documentation: https://docs.n8n.io/

---

**Document Version**: 1.0  
**Last Updated**: June 12, 2026  
**Maintained By**: QA Automation Team  
**Next Review**: September 12, 2026
