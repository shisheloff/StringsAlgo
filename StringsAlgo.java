import java.util.LinkedList;

class StringsAlgo{
    public static void main(String[] args) {
        String text = "when you start thinking a lot about your past, it becomes your present and you can't see your future without it";
        String pat = "it";
        String pat2 = "when";
        String pat3 = "then";
        String pat4 = "our";
        System.out.println("---------------------------------------");
        System.out.println("Rabin-Karp algorithm; pattern: " + pat);
        print(rabinKarp(pat, text, 101));
        System.out.println("Knutt-Morris-Pratt algorithm; pattern: " + pat3);
        print(KMP(pat3, text));
        System.out.println("Boyer-Moore algorithm; pattern: " + pat4);
        print(BM(text.toCharArray(), pat4.toCharArray()));
        
        System.out.println("Rabin-Karp algorithm; pattern: " + pat2);
        print(rabinKarp(pat2, text, 101));
        System.out.println("---------------------------------------");

    }

    // Алгоритм Рабина-Карпа
    public static LinkedList<Integer> rabinKarp (String findString, String text, int prime) {
        int d = 256;        // количество символов в вводимом алфавите

        int findStringLength = findString.length();     // длина шаблона
        int textLength = text.length();                 // длина текста
        int fsHash = 0;                                 // переменная для хранения хеша шаблона
        int txtHash = 0;                                // переменная для хранения хеша текста
        int h = 1;
        int i, j;
        LinkedList<Integer> result = new LinkedList<Integer>(); // список для хранения индексов вхождения шаблона

        // h = pow(d, findStringLength -1) % prime
        for (i = 0; i < findStringLength - 1; i++){
            h = (h * d) % prime;
        }

        // вычисление значения hash
        for (i = 0; i < findStringLength; i++){
            fsHash = (d * fsHash + findString.charAt(i)) % prime;
            txtHash = (d * txtHash + text.charAt(i)) % prime;
        }

        for (i = 0; i <= textLength - findStringLength; i++ ) {
            if (fsHash == txtHash){
                for (j = 0; j < findStringLength; j++) {
                    if (text.charAt(i + j) != findString.charAt(j))
                        break;
                }

                if (j == findStringLength)
                   // System.out.println("String found at index: " + i);
                   result.add(i);
                   
            }

            if (i < textLength - findStringLength){
                txtHash = (d * (txtHash - text.charAt(i) * h) + text.charAt(i + findStringLength)) % prime;
                if (txtHash < 0)
                    txtHash = (txtHash + prime);
            }
        }
        return result;
    }

    // Алгоритм Кнута-Морриса-Пратта
    public static LinkedList<Integer> KMP (String findString, String text){
        LinkedList<Integer> result = new LinkedList<Integer>();
        int fsLen = findString.length();
        int textLength = text.length();
        int longestPrefixSuffix[] = new int[fsLen];
        int ind = 0;

        LPSArray(findString, fsLen, longestPrefixSuffix);

        int i = 0;
        while (i < textLength){
            if (findString.charAt(ind) == text.charAt(i)){
                ind++;
                i++;
            }
            if (ind == fsLen){
               result.add(i - ind); 
               ind = longestPrefixSuffix[ind - 1];
                
            }
            else if (i < textLength && findString.charAt(ind) != text.charAt(i)){
                if (ind != 0)
                    ind = longestPrefixSuffix[ind - 1];
                else 
                    i++;
            }
        }

        return result;
    }
    private static void LPSArray(String findString, int fsLen, int lps[]){
        int len = 0;
        int i = 1;
        lps[0] = 0;
        while(i<fsLen){

            if (findString.charAt(i) == findString.charAt(len)){
                len++;
                lps[i] = len;
                i++;

            } else {   

                if (len != 0)
                    len = lps[len - 1];

                else 
                    lps[i] = len;
                    i++;
            }
        }
    }

    // Алгоритм Бойера - Мура
    public static LinkedList<Integer> BM(char[] text, char[] findString){
        LinkedList<Integer> result = new LinkedList<Integer>();
        int sh = 0, j;
        int fs = findString.length;
        int t = text.length;
        
        int[] bpos = new int[fs + 1];
        int[] shift = new int[fs + 1];

        for (int i = 0; i < fs + 1; i++) {
            shift[i] = 0;
        }

        prep_str_suff(shift, bpos, findString, fs);
        prep_c2(shift, bpos, findString, fs);

        while (sh <= t - fs){
            j = fs - 1;
            while (j >= 0 && findString[j] == text[sh + j])
                j--;
            if (j < 0){
                result.add(sh);
                sh += shift[0];
            } else {
                sh += shift[j + 1];
            }
        }
        return result;
    }
    private static void prep_str_suff(int[] shift, int[] bpos, char[] findString, int fs){
        int i = fs;
        int j = fs + 1;
        bpos[i] = j;

        while (i > 0){
            while (j <= fs && findString[i - 1] != findString[j - 1]){
                if (shift[j] == 0)
                    shift[j] = j - i;
                
                j = bpos[j];
            }
            i--; j--;
            bpos[i] = j;
        }
    }
    private static void prep_c2(int[] shift, int[] bpos, char[] findString, int fs){
        int i,j;
        j = bpos[0];
        for (i = 0; i < fs; i++){
            if (shift[i] == 0)
                shift[i] = j;
            
            if (i == j)
                j = bpos[j];
            
        }
    }

    // вывод индексов вхождений образца
    public static void print(LinkedList<Integer> ll){
        for (int i = 0; i < ll.size(); i++){
            System.out.println("Pattern found at index: " + ll.get(i));
        }
        System.out.println();
    }
}