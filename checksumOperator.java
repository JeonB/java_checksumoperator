package exercise;

import java.util.Scanner;
import java.util.Arrays;

public class checksumOperator {
    static int[] checksum = new int[4];
    static int[] carry = new int[5];

    public static void main(String[] args) {

        int[] data = new int[28]; // 2진수로 변환된 학번을 담는 배열
        int[] data2 = new int[8]; // 4비트 블록들의 합을 2진수로 변환시킨 값을 담는 배열
        int[] data3 = new int[8]; // checksum을 더한 데이터의 4비트 블록 합을 2진수로 변환시킨 값을 담는 배열
        int[] checksumData = new int[4]; // checksum을 더한 데이터 블록의 합
        int sum = 0;
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);

        System.out.print("학번 입력: ");
        int studentID = sc.nextInt(); // 학번입력
        transDecToBin(data, studentID);

        System.out.print("2진수로 변환된 4비트 블록 학번 : ");
        strBlock(data); // 4비트 블록 출력

        System.out.print("\n" + "10진수로 변환된 4비트 블록 : ");
        for (int i = 0; i < 7; i++) {
            System.out.print("    " + transBinToDec(data, i));
            sum += transBinToDec(data, i);
        }

        System.out.print("\n4비트 블록들의 합 : " + sum + " -> ");
        transDecToBin(data2, sum); // 2진수 변환
        strBlock(data2); // 4비트 블록 출력

        // checksum 계산
        checksum = arrSum(selectBlock(data2, 1), selectBlock(data2, 2));
        checksumOp(checksum); // checksum 계산 메소드 호출
        System.out.println("\nchecksum: " + Arrays.toString(checksum) + " -> " + transBinToDec(checksum, 0));

        // checksum 검증
        sum += transBinToDec(checksum, 0);
        System.out.print("checksum을 더한 데이터 합 :" + sum + " -> ");
        transDecToBin(data3, sum); // 2진수 변환
        strBlock(data3); // 4비트 블록 출력
        checksumData = arrSum(selectBlock(data3, 1), selectBlock(data3, 2));
        System.out.println("\nchecksum을 더한 데이터 블록의 합 : " + Arrays.toString(checksumData));
        System.out.print("checksum 검증 : ");
        checksumCheck(checksumData); // checksum 검증 메소드 호출
    }

    // 10진수를 2진수로 변환하는 메소드
    public static void transDecToBin(int[] bitArr, int num) {
        int n = 0;
        int powOf2; // 2의 거듭제곱

        while (true) {
            powOf2 = (int) Math.pow(2, n);
            if (num > powOf2) {
                ++n;
            } else if (num < powOf2) {
                --n;
                powOf2 = (int) Math.pow(2, n);
                num = num - powOf2;
                bitArr[(bitArr.length - 1) - n] = 1;
                n = 0;
            } else {
                bitArr[(bitArr.length - 1) - n] = 1;
                break;
            }

        }

    }

    // 2진수 4비트 블록을 10진수로 변환하는 메소드
    public static int transBinToDec(int[] bitArr, int a) {

        int[] dec = new int[7];
        int[][] block = new int[7][4];
        int n = 0, k = 0;

        for (int i = 0; i < bitArr.length; i += 4) {
            block[n] = Arrays.copyOfRange(bitArr, i, i + 4);
            for (int j = 0; j < 4; j++) {
                dec[k] += block[n][j] * (int) Math.pow(2, 3 - j);
            }
            n++;
            k++;
        }

        return dec[a];
    }

    // 4비트 블록을 String type으로 반환하는 메소드
    public static void strBlock(int[] bitArr) {
        int[] block;
        String str;

        for (int i = 0; i < bitArr.length; i += 4) {
            block = Arrays.copyOfRange(bitArr, i, i + 4);
            str = Arrays.toString(block);
            str = str.substring(1, str.length() - 1).replace(", ", "");
            System.out.print(str + " ");
        }

    }

    // 4비트 블록중 선택한 한 블록을 반환하는 메소드
    public static int[] selectBlock(int[] bitArr, int num) {

        int[][] block = new int[4][7];
        int j = 1;

        for (int i = 0; i < bitArr.length; i += 4) {
            block[j] = Arrays.copyOfRange(bitArr, i, i + 4);
            j++;
        }

        return block[num];
    }

    // 입력한 블록(2진수 배열)을 더하는 메소드
    public static int[] arrSum(int[] arr1, int[] arr2) {
        int[] sum = new int[4];
        int carry2[] = new int[5];
        int j = 4;
        for (int i = 3; i > -1; i--) {

            if (carry2[j] == 0) {
                if (arr1[i] + arr2[i] == 2) {
                    sum[i] = 0;
                    carry2[j - 1] = 1;
                } else if (arr1[i] + arr2[i] == 0) {
                    sum[i] = 0;
                } else {
                    sum[i] = 1;
                }
            } else {
                if (carry2[j] + arr1[i] + arr2[i] == 3) {
                    sum[i] = 1;
                    carry2[j - 1] = 1;
                } else if (carry2[j] + arr1[i] + arr2[i] == 2) {
                    sum[i] = 0;
                    carry2[j - 1] = 1;
                } else {
                    sum[i] = 1;
                }
            }
            j--;
        }
        carry[j] = carry2[j];
        return sum;
    }

    // checksum 계산기
    public static int[] checksumOp(int[] checksum2) {
        if (carry[0] == 1) {
            int[] carry2 = { 0, 0, 0, 1 };
            checksum = arrSum(carry2, checksum2);
        }
        for (int i = 0; i < checksum2.length; i++) {
            if (checksum[i] == 0) {
                checksum[i] = 1;
            } else {
                checksum[i] = 0;
            }
        }

        return checksum;
    }

    // checksum 검증 메소드
    public static void checksumCheck(int[] checksum2) {

        int[] check = { 0, 0, 0, 1 };
        int[] check2 = { 0, 0, 0, 0 };
        int[] checkData = arrSum(checksum2, check);
        if (Arrays.equals(check2, checkData)) {
            System.out.println("checksum 검증 성공!");
        } else {
            System.out.println("checksum 검증 실패!");
        }
    }

}