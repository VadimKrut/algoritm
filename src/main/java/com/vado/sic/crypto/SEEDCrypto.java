package com.vado.sic.crypto;

import com.vado.sic.util.ChartUtil;
import com.vado.sic.util.ExcelUtil;
import com.vado.sic.util.LoadFileUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class SEEDCrypto {

    private static final String ALGORITHM = "SEED";

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            int[] trials = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            int trialsPerValue = 5;
            assayIteration(trials, trialsPerValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void assayIteration(int[] trials, int trialsPerValue) {
        List<List<String>> allData = new ArrayList<>();
        List<Integer> dataSizes = new ArrayList<>();
        List<Long> encryptTimes = new ArrayList<>();
        List<Long> decryptTimes = new ArrayList<>();
        List<Long> totalTimes = new ArrayList<>();
        try {
            for (int i : trials) {
                byte[] array = LoadFileUtil.getBytes(i);
                List<List<String>> data = assay(trialsPerValue, array);
                dataSizes.add(array.length);
                encryptTimes.add(Long.valueOf(data.get(trialsPerValue).get(3)));
                decryptTimes.add(Long.valueOf(data.get(trialsPerValue).get(4)));
                totalTimes.add(Long.valueOf(data.get(trialsPerValue).get(5)));
                allData.addAll(data);
            }
            ChartUtil.createChartAndSave(dataSizes, encryptTimes, decryptTimes, totalTimes,
                    ALGORITHM, ALGORITHM);
            ExcelUtil.createExcelFile(ALGORITHM, ALGORITHM, allData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<List<String>> assay(int trials, byte[] array) {
        List<List<String>> data = new ArrayList<>();
        List<Long> encryptTimes = new ArrayList<>();
        List<Long> decryptTimes = new ArrayList<>();
        List<Long> totalTimes = new ArrayList<>();

        try {
            for (int x = 1; x <= trials; x++) {
                List<String> iterationData = iteration(array);
                data.add(iterationData);

                long encryptTime = Long.parseLong(iterationData.get(3));
                long decryptTime = Long.parseLong(iterationData.get(4));
                long totalTime = Long.parseLong(iterationData.get(5));

                encryptTimes.add(encryptTime);
                decryptTimes.add(decryptTime);
                totalTimes.add(totalTime);
            }

            long avgEncryptTime = calculateAverage(encryptTimes);
            long avgDecryptTime = calculateAverage(decryptTimes);
            long avgTotalTime = calculateAverage(totalTimes);

            List<String> avgData = new ArrayList<>();
            avgData.add(ALGORITHM + " (Average)");
            avgData.add("");
            avgData.add("");
            avgData.add(String.valueOf(avgEncryptTime));
            avgData.add(String.valueOf(avgDecryptTime));
            avgData.add(String.valueOf(avgTotalTime));
            data.add(avgData);

            data.add(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static long calculateAverage(List<Long> times) {
        long total = 0;
        for (long time : times) {
            total += time;
        }
        return total / times.size();
    }

    private static SecretKey generateKey() throws Exception {
        return new SecretKeySpec(org.bouncycastle.util.encoders.Hex.decode("000102030405060708090A0B0C0D0E0F1011121314151617"), ALGORITHM);
    }

    private static List<String> iteration(byte[] array) {
        List<String> result = new ArrayList<>();
        try {
            long startEncrypt = System.currentTimeMillis();
            SecretKey secretKey = generateKey();
            byte[] encryptedBytes = encryptTime(array, secretKey);
            long endEncrypt = System.currentTimeMillis();
            long encryptTime = endEncrypt - startEncrypt;

            long startDecrypt = System.currentTimeMillis();
            byte[] decryptedBytes = decryptTime(encryptedBytes, secretKey);
            long endDecrypt = System.currentTimeMillis();
            long decryptTime = endDecrypt - startDecrypt;

            long endTime = System.currentTimeMillis();

            result.add(ALGORITHM);
            result.add(String.valueOf(encryptedBytes.length));
            result.add(String.valueOf(decryptedBytes.length));
            result.add(String.valueOf(encryptTime));
            result.add(String.valueOf(decryptTime));
            result.add(String.valueOf(endTime - startEncrypt));

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] encrypt(byte[] info, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(info);
    }

    private static byte[] decrypt(byte[] cipherText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(cipherText);
    }

    private static byte[] encryptTime(byte[] originalMessage, SecretKey secretKey) throws Exception {
        return encrypt(originalMessage, secretKey);
    }

    private static byte[] decryptTime(byte[] encryptedBytes, SecretKey secretKey) throws Exception {
        return decrypt(encryptedBytes, secretKey);
    }
}