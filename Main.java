import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
public class Main {
    public static void main(String[] args) {
        String text = "abbccbbaaccc";

        TreeMap<Character, Integer> frequencies = countFrequency(text);

        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();

        for (Character c : frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }
        CodeTreeNode tree = huffman(codeTreeNodes);

        TreeMap<Character, String> codes = new TreeMap<>();

        for (Character c : frequencies.keySet()) {
            codes.put(c, tree.getCodeForCharacter(c, ""));
        }
        System.out.println(codes.toString());

        StringBuilder encoded = new StringBuilder();
        for(int i = 0; i<text.length(); i++){
            encoded.append(codes.get(text.charAt(i)));
        }

        System.out.println("Start size: " + text.getBytes().length*8 + " bit");
        System.out.println("End size: " + encoded.length() + " bit");

        String decoded = huffmanDecode(encoded.toString(), tree);

        System.out.println("Decoded: " + decoded);
    }

    private static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> freqMap = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = freqMap.get(c);
            freqMap.put(c, count != null ? count + 1 : 1);
        }
        return freqMap;
    }

    private static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            CodeTreeNode left = codeTreeNodes.remove(codeTreeNodes.size() - 1);
            CodeTreeNode right = codeTreeNodes.remove(codeTreeNodes.size() - 1);

            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right);

            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }
    public static String huffmanDecode(String encoded, CodeTreeNode tree){
        StringBuilder decoded = new StringBuilder();

        CodeTreeNode node = tree;

        for(int i = 0; i<encoded.length(); i++){
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if(node.data != null){
                decoded.append(node.data);
                node = tree;
            }
        }
        return decoded.toString();
    }

    private static class CodeTreeNode implements Comparable<CodeTreeNode> {
        Character data;
        int weight;
        CodeTreeNode right;
        CodeTreeNode left;

        public CodeTreeNode(Character data, int weight) {
            this.data = data;
            this.weight = weight;
        }

        public CodeTreeNode(Character data, int weight, CodeTreeNode left, CodeTreeNode right) {
            this.data = data;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            return Integer.compare(o.weight, weight);
        }

        public String getCodeForCharacter(Character ch, String parentPath) {
            if (data == ch) {
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + 0);
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + 1);
                    if (path != null) {
                        return path;
                    }
                }
            }
            return null;
        }
    }
}
