package ua.edu.ukma.generators.keys.string;

import ua.edu.ukma.generators.AbstractPrecomputedGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UniformEmailKeyGenerator extends AbstractPrecomputedGenerator<String> {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char AT = '@';

    public UniformEmailKeyGenerator(int numberOfKeys, int prefixLength, int domainsCount, long seed) {
        super(computeKeys(numberOfKeys, prefixLength, domainsCount, seed));
    }

    private static String[] computeKeys(int numberOfKeys, int prefixLength, int domainsCount, long seed) {
        Random random = new Random(seed);
        String[] keys = new String[numberOfKeys];
        String[] domains = generateDomains(domainsCount);
        for (int i = 0; i < numberOfKeys; i++) {
            keys[i] = generateString(prefixLength, random)
                    .append(AT)
                    .append(domains[random.nextInt(domainsCount)])
                    .toString();
        }
        return keys;
    }

    private static String[] generateDomains(int domainsCount) {
        String[] domains = new String[domainsCount];
        for (int i = 0; i < domainsCount; i++) {
            String suffix = i % 2 == 0 ? ".com" : ".edu.ua";
            domains[i] = String.format("%03d", i) + suffix;
        }
        return domains;
    }

    private static StringBuilder generateString(int length, Random random) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }
        return sb;
    }

    public static String[] generateKeySpace(int prefixLength, int domainsCount) {
        String[] domains = generateDomains(domainsCount);
        List<String> prefixes = generatePrefixes(prefixLength);
        String[] keySpace = new String[prefixes.size() * domainsCount];
        for (int i = 0; i < prefixes.size(); i++) {
            String prefix = prefixes.get(i);
            for (int j = 0; j < domainsCount; j++) {
                keySpace[i * domainsCount + j] = prefix + AT + domains[j];
            }
        }
        return keySpace;
    }

    private static List<String> generatePrefixes(int prefixLength) {
        List<String> prefixes = List.of("");
        for (int i = 0; i < prefixLength; i++) {
            List<String> newPrefixes = new ArrayList<>(prefixes.size() * ALPHABET.length);
            for (String prefix : prefixes) {
                for (char c : ALPHABET) {
                    newPrefixes.add(prefix + c);
                }
            }
            prefixes = newPrefixes;
        }
        return prefixes;
    }
}
