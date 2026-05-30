package PracticePgms;

public class ReverseString {
    // reversing words also trying for that

public static void main(String[] args) {


    String  ABC= "I love Java"; //declaring a string
    String[] words=ABC.split(" "); //splitting a string
    String Result ="";          //taking result variable

    for(int i=words.length -1;i>=0;i--) // traversing through the words for reversing words
    {
        Result+=words[i];
        if(i !=0 )
            Result+=" ";
    }

    System.out.println("Result:-"+Result);
}

}
