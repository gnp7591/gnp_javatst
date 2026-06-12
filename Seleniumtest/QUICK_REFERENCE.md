# 🚀 Quick Reference Guide
**agents.md Framework - Daily Usage**

---

## ⏱️ 60-SECOND OVERVIEW

**What happens automatically every day at 8 AM:**

1. **Tests Run** (45 seconds)
   - Maven executes 14 smoke tests
   - Chrome browser opens automatically
   - Tests navigate, click, assert

2. **Report Generated** (3 seconds)
   - Parse test results
   - Calculate pass rate
   - Identify failures

3. **Email Sent** (1 second)
   - Rich HTML email to: **gnpuranik479@gmail.com**
   - Contains all test details
   - Ready to act on

**Total Time:** ~1-2 minutes ✅

---

## 📧 WHAT YOU'LL RECEIVE

### ✅ PASS EMAIL (Pass rate ≥ 95%)
```
Subject: ✅ Test Automation Report | 14/14 Passed (100%)

Highlights:
• All tests passing
• No failures
• Green light to proceed
```

### ⚠️ WARN EMAIL (Pass rate 90-95%)
```
Subject: ⚠️ Test Automation Report | 13/14 Passed (93%)

Highlights:
• Most tests passing
• 1 failure details
• Investigate recommended failure
```

### ❌ FAIL EMAIL (Pass rate < 90%)
```
Subject: ❌ Test Automation Report | 9/14 Passed (64%) - ATTENTION REQUIRED

Highlights:
• Multiple failures
• Root cause analysis
• Urgent recommended actions
• 7-day trend showing degradation
• Links to full report
```

---

## 🔍 READING THE EMAIL

### Key Sections in Order:

1. **Red Alert Box** - Quick status (pass/fail/warning)
2. **Metrics Dashboard** - Visual gauges (passed, failed, %, duration)
3. **Failed Tests Table** - Each failure with error + recommendation
4. **Root Cause Hypotheses** - Why each test failed
5. **Trend Chart** - 7-day pass rate history
6. **Recommendations** - What to do next (by priority)
7. **Report Links** - Full Allure dashboard and logs

---

## 📞 QUICK ACTIONS

### If Email Says ❌ FAIL:

**Immediate (< 5 min):**
```bash
1. Read "Root Cause Analysis" section in email
2. Check "Recommended Actions"
3. Open "View Full Allure Report" link
4. Look at failed test screenshots
```

**Short-term (< 30 min):**
```bash
1. Share email with dev team
2. Create bug tickets for each failure
3. Discuss root causes

# Example:
# Subject: BUG - Cart add to cart failing (Test Automation)
# Description: See email from Test Automation Framework
```

**Verification (< 4 hours):**
```bash
1. Dev team fixes issues
2. Run smoke tests manually:
   cd c:\Users\gnpur\git\gnp_javatst\Seleniumtest
   mvn -D"suiteXmlFile=testng-smoke.xml" clean test

3. Verify pass rate improved
```

---

## 🏃 MANUAL TEST RUN

### Quick Smoke Test (9 PM)
```bash
cd c:\Users\gnpur\git\gnp_javatst\Seleniumtest
mvn -D"suiteXmlFile=testng-smoke.xml" clean test
```

### All UI Tests (detailed)
```bash
cd c:\Users\gnpur\git\gnp_javatst\Seleniumtest
mvn -D"suiteXmlFile=testng-all-uitests.xml" clean test
```

### View Report Locally
```bash
cd c:\Users\gnpur\git\gnp_javatst\Seleniumtest
mvn allure:serve
# Opens: http://localhost:4040
```

---

## 📊 UNDERSTANDING METRICS

| Metric | Green | Yellow | Red |
|--------|-------|--------|-----|
| **Pass Rate** | ≥ 95% | 90-94% | < 90% |
| **Duration** | < 30s | 30-60s | > 60s |
| **New Failures** | 0 | 1-2 | > 2 |
| **Flaky Tests** | 0 | 1 | > 1 |
| **Trend** | ↑ or → | Stable | ↓ |

---

## 🎯 FAILURE SEVERITY LEVELS

### 🔴 CRITICAL - Must Fix NOW
- E-commerce core feature failing
- Authentication broken
- System crash or block

**Action:** Fix within 1-2 hours

### 🟠 HIGH - Fix This Week
- Important feature partially broken
- Performance degradation
- Security concern

**Action:** Fix within 24 hours

### 🟡 MEDIUM - Plan Fix Soon
- Minor feature issue
- Inconsistent behavior (flaky)
- UI refinement

**Action:** Fix within this week

### 🔵 LOW - Track for Later
- Edge case issue
- Nice-to-have enhancement
- Documentation update

**Action:** Add to backlog

---

## 📈 TREND INTERPRETATION

### 📈 IMPROVING (Pass Rate ↑)
- Recent fixes are working
- Product quality increasing
- Keep monitoring

### ➡️ STABLE (Pass Rate →)
- Consistent quality
- No regressions detected
- Normal operation

### 📉 DEGRADING (Pass Rate ↓)
- New bugs introduced
- Code changes causing failures
- Investigation needed

**Response:** Contact dev team if sustained drop > 5%

---

## ❓ FAQ

### Q: How often do tests run?
**A:** Daily at 8 AM (configurable). Manual runs anytime via command line.

### Q: Do I need to do anything?
**A:** No! Tests run automatically. Just check email reports and act on failures.

### Q: What if all tests pass?
**A:** You get a ✅ green email. Everything is good. No action needed.

### Q: What if I need detailed info?
**A:** Click "View Full Allure Report" link in email for screenshots, logs, detailed data.

### Q: Can I run tests outside daily schedule?
**A:** Yes! Run anytime:
```bash
mvn -D"suiteXmlFile=testng-smoke.xml" clean test
```

### Q: How long do tests take?
**A:** ~45 seconds for smoke suite (14 tests)

### Q: What browsers are tested?
**A:** Currently Chrome only (configurable to add Firefox, Safari, Edge)

### Q: Are there other test suites?
**A:** Yes:
- `testng-smoke.xml` - Quick smoke tests (14 tests, 45s)
- `testng-grouped.xml` - Tests grouped by component
- `testng-all-uitests.xml` - Complete UI test suite
- `testng.xml` - Default suite

### Q: Where are test reports stored?
**A:** 
- Local: `target/allure-results/` and `target/allure-report/`
- Cloud: S3 or Azure Blob (when deployed)
- Database: PostgreSQL (for trend analysis)

### Q: How do I share reports with team?
**A:** Email is sent automatically to gnpuranik479@gmail.com
Include report link when sharing: `http://localhost:4040/report/`

---

## 🛠️ TROUBLESHOOTING

### Issue: No email received

**Check:**
1. Gmail inbox (check spam folder)
2. NOTIFICATION_EMAIL is set to gnpuranik479@gmail.com
3. Gmail App Password is configured (not regular password)
4. SMTP server is accessible (smtp.gmail.com:587)

### Issue: Tests fail but I expected them to pass

**Check:**
1. Are you looking at the right report? (Date/time stamp)
2. Did code change recently? (Check git diff)
3. Are tests flaky? (Check if they pass on re-run)
4. Is browser/driver updated? (Check Chrome version)

### Issue: Report hasn't arrived by 8:30 AM

**Check:**
1. Is n8n workflow running? (Check n8n UI → Executions)
2. Did tests complete successfully? (Check Maven logs)
3. Is Report Analyzer stuck? (Check n8n execution time)

### Issue: Tests running slow (> 1 minute)

**Check:**
1. Is system under heavy load?
2. Are tests waiting for long timeouts?
3. Is Chrome using lots of memory?
4. Is disk I/O bottleneck?

---

## 📌 IMPORTANT DATES

- **Next Daily Run:** Tomorrow at 8:00 AM UTC+5:30
- **Weekly Summary:** Every Friday (automated in future phase)
- **Monthly Report:** End of month (automated in future phase)
- **Framework Review:** Quarterly (or when major changes made)

---

## 📞 CONTACT INFO

**For Framework/Setup Help:**
- Contact: QA Automation Team

**For Test Failures:**
- Contact: Development Team (with email report)

**For Infrastructure Issues:**
- Contact: DevOps Team (n8n, database, cloud storage)

---

## 🎯 SUCCESS CRITERIA

✅ Framework working well when:
- Emails arrive daily at 8 AM
- Pass rate stays ≥ 90%
- No flaky tests (or < 1 flaky)
- Failures are actionable (clear root cause)
- Team responds to failures within 4 hours

📊 Report effectiveness:
- Team can identify root cause in < 10 minutes
- All critical bugs caught automatically
- Trends visible at a glance
- Confidence in test reliability

---

## 📚 FULL DOCUMENTATION

See these files for complete details:
- `agents.md` - Framework definition
- `SAMPLE_TEST_REPORT.md` - Report structure
- `SAMPLE_EMAIL_TEMPLATE.html` - Email format
- `WORKFLOW_ORCHESTRATION_GUIDE.md` - Complete flow
- `ISSUES_OBSERVED.md` - Action items from sample run
- `SAMPLE_RUN_SUMMARY.md` - Full overview

---

**Quick Reference v1.0**  
**Last Updated:** June 12, 2026  
**Status:** Ready to Use ✅
