// Java Libraries Declaration
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VisualGo extends JFrame {

    private final VisualPanel visualPanel = new VisualPanel();
    private final JTextArea codeArea = new JTextArea();

    private final JButton bubbleSortButton = new JButton("Bubble Sort");
    private final JButton selectionSortButton = new JButton("Selection Sort");
    private final JButton insertionSortButton = new JButton("Insertion Sort");
    private final JButton mergeSortButton = new JButton("Merge Sort");

    private final JButton linearSearchButton = new JButton("Linear Search");
    private final JButton binarySearchButton = new JButton("Binary Search");
    private final JButton jumpSearchButton = new JButton("Jump Search");
    private final JButton interpolationSearchButton = new JButton("Interpolation Search");

    private final JButton setArrayButton = new JButton("Set Array");
    private final JButton resetButton = new JButton("Reset");

    private final JButton prevButton = new JButton("Prev");
    private final JButton playButton = new JButton("Play");
    private final JButton nextButton = new JButton("Next");


    private final JRadioButton sortingMode = new JRadioButton("Sorting", true);
    private final JRadioButton searchingMode = new JRadioButton("Searching");
    private final ButtonGroup modeGroup = new ButtonGroup();

    private int[] array = {};
    private final ArrayList<Step> steps = new ArrayList<>();
    private int currentStep = -1;
    private volatile boolean playing = false;

    public VisualGo() {
        setTitle("VisualGo: Searching and Sorting Visualizer");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        titlePanel.setBackground(new Color(37, 55, 69));

        JLabel titlePart1 = new JLabel("VisuALGO:");
        titlePart1.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titlePart1.setForeground(new Color(255, 143, 0));

        JLabel titlePart2 = new JLabel("Searching and Sorting Algorithms Visualizer");
        titlePart2.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        titlePart2.setForeground(Color.WHITE);

        titlePanel.add(titlePart1);
        titlePanel.add(titlePart2);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel topControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        modeGroup.add(sortingMode);
        modeGroup.add(searchingMode);
        stylePrimaryButton(setArrayButton);
        stylePrimaryButton(resetButton);

        topControls.add(new JLabel("Mode:"));
        topControls.add(sortingMode);
        topControls.add(searchingMode);
        topControls.add(setArrayButton);
        topControls.add(resetButton);
        topPanel.add(topControls, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        JPanel sidePanel = new JPanel(new CardLayout());
        sidePanel.setBorder(BorderFactory.createTitledBorder("Algorithms"));
        sidePanel.setPreferredSize(new Dimension(200, 500));

        JPanel sortingPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (JButton b : new JButton[]{bubbleSortButton, selectionSortButton, insertionSortButton, mergeSortButton}) {
            stylePrimaryButton(b);
            sortingPanel.add(b);
        }

        JPanel searchingPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (JButton b : new JButton[]{linearSearchButton, binarySearchButton, jumpSearchButton, interpolationSearchButton}) {
            stylePrimaryButton(b);
            searchingPanel.add(b);
        }

        sidePanel.add(sortingPanel, "SORTING");
        sidePanel.add(searchingPanel, "SEARCHING");
        add(sidePanel, BorderLayout.WEST);

        // ===== CENTER PANEL =====
        JPanel mainArea = new JPanel(new GridLayout(1, 2, 10, 10));

        visualPanel.setBackground(new Color(204, 208, 207));
        visualPanel.setBorder(BorderFactory.createTitledBorder("Visualizer"));
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        codeArea.setEditable(false);
        JScrollPane codeScroll = new JScrollPane(codeArea);
        codeScroll.setBorder(BorderFactory.createTitledBorder("Algorithm Code"));

        mainArea.add(visualPanel);
        mainArea.add(codeScroll);
        add(mainArea, BorderLayout.CENTER);

        // ===== BOTTOM CONTROLS =====
        JPanel bottomControls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        styleSecondaryButton(prevButton);
        styleSecondaryButton(playButton);
        styleSecondaryButton(nextButton);
        bottomControls.add(prevButton);
        bottomControls.add(playButton);
        bottomControls.add(nextButton);
        add(bottomControls, BorderLayout.SOUTH);

        // ===== EVENT HANDLERS =====
        CardLayout cl = (CardLayout) sidePanel.getLayout();
        sortingMode.addActionListener(e -> cl.show(sidePanel, "SORTING"));
        searchingMode.addActionListener(e -> cl.show(sidePanel, "SEARCHING"));

        setArrayButton.addActionListener(e -> setArray());
        resetButton.addActionListener(e -> resetVisualizer());
        prevButton.addActionListener(e -> showStep(currentStep - 1));
        nextButton.addActionListener(e -> showStep(currentStep + 1));
        playButton.addActionListener(e -> togglePlay());

        bubbleSortButton.addActionListener(e -> runAlgorithm("Bubble Sort"));
        selectionSortButton.addActionListener(e -> runAlgorithm("Selection Sort"));
        insertionSortButton.addActionListener(e -> runAlgorithm("Insertion Sort"));
        mergeSortButton.addActionListener(e -> runAlgorithm("Merge Sort"));

        linearSearchButton.addActionListener(e -> runAlgorithm("Linear Search"));
        binarySearchButton.addActionListener(e -> runAlgorithm("Binary Search"));
        jumpSearchButton.addActionListener(e -> runAlgorithm("Jump Search"));
        interpolationSearchButton.addActionListener(e -> runAlgorithm("Interpolation Search"));

        setVisible(true);
    }

    // ===== SET ARRAY =====
    private void setArray() {
        String input = JOptionPane.showInputDialog(this, "Enter numbers separated by commas:", "Set Array", JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.isEmpty()) {
            try {
                String[] parts = input.split(",");
                array = Arrays.stream(parts).map(String::trim).mapToInt(Integer::parseInt).toArray();
                visualPanel.setArray(array, -1, -1);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter integers separated by commas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== RESET =====
    private void resetVisualizer() {
        array = new int[]{};
        steps.clear();
        currentStep = -1;
        visualPanel.setArray(array, -1, -1);
        codeArea.setText("");
    }

    // ===== STEP MANAGEMENT =====
    private void recordStep(int[] arr, int h1, int h2, String description) {
        steps.add(new Step(arr.clone(), h1, h2, description));
    }

    private void showStep(int i) {
        if (steps.isEmpty() || i < 0 || i >= steps.size()) return;
        currentStep = i;
        Step s = steps.get(i);
        visualPanel.setArray(s.arr, s.h1, s.h2);
        codeArea.setText(s.description);
        prevButton.setEnabled(i > 0);
        nextButton.setEnabled(i < steps.size() - 1);
    }

    private void togglePlay() {
        if (playing) {
            playing = false;
            playButton.setText("▶ Play");
            return;
        }
        playing = true;
        playButton.setText("■ Stop");

        new Thread(() -> {
            for (int i = currentStep + 1; i < steps.size() && playing; i++) {
                final int idx = i;
                SwingUtilities.invokeLater(() -> showStep(idx));
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
            playing = false;
            SwingUtilities.invokeLater(() -> playButton.setText("▶ Play"));
        }).start();
    }

    // ===== RUN ALGORITHM =====
    private void runAlgorithm(String algo) {
        if (array == null || array.length == 0) {
            JOptionPane.showMessageDialog(this, "Please set the array first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        steps.clear();
        currentStep = -1;

        switch (algo) {
            case "Bubble Sort": generateBubbleSortSteps(); break;
            case "Selection Sort": generateSelectionSortSteps(); break;
            case "Insertion Sort": generateInsertionSortSteps(); break;
            case "Merge Sort": generateMergeSortSteps(); break;
            case "Linear Search":
            case "Binary Search":
            case "Jump Search":
            case "Interpolation Search":
                String input = JOptionPane.showInputDialog(this, "Enter the number to search:", "Search", JOptionPane.PLAIN_MESSAGE);
                if (input == null || input.isEmpty()) return;
                try {
                    int target = Integer.parseInt(input);
                    switch (algo) {
                        case "Linear Search": generateLinearSearchSteps(target); break;
                        case "Binary Search": generateBinarySearchSteps(target); break;
                        case "Jump Search": generateJumpSearchSteps(target); break;
                        case "Interpolation Search": generateInterpolationSearchSteps(target); break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                break;
        }
        if (!steps.isEmpty()) showStep(0);
    }

    // ===== SORTING ALGORITHMS =====
    private void generateBubbleSortSteps() {
        int[] a = array.clone();
        String code = "// Bubble Sort Implementation\nfor (int i = 0; i < n - 1; i++) {\n    for (int j = 0; j < n - i - 1; j++) {\n        if (arr[j] > arr[j + 1]) swap(arr[j], arr[j + 1]);\n    }\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Bubble Sort...");

        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t;
                    recordStep(a, j, j + 1, code + "\nSwapped elements at indices " + j + " and " + (j + 1));
                } else {
                    recordStep(a, j, j + 1, code + "\nCompared elements at indices " + j + " and " + (j + 1));
                }
            }
        }
    }

    private void generateSelectionSortSteps() {
        int[] a = array.clone();
        String code = "// Selection Sort Implementation\nfor (int i = 0; i < n - 1; i++) {\n    int min = i;\n    for (int j = i + 1; j < n; j++) {\n        if (arr[j] < arr[min]) min = j;\n    }\n    swap(arr[i], arr[min]);\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Selection Sort...");

        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) min = j;
                recordStep(a, min, j, code + "\nSelecting new minimum at index " + min);
            }
            int t = a[min]; a[min] = a[i]; a[i] = t;
            recordStep(a, i, min, code + "\nSwapped elements at indices " + i + " and " + min);
        }
    }

    private void generateInsertionSortSteps() {
        int[] a = array.clone();
        String code = "// Insertion Sort Implementation\nfor (int i = 1; i < n; i++) {\n    int key = arr[i];\n    int j = i - 1;\n    while (j >= 0 && arr[j] > key) {\n        arr[j + 1] = arr[j];\n        j--;\n    }\n    arr[j + 1] = key;\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Insertion Sort...");

        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
                recordStep(a, i, j, code + "\nShifting element at index " + j);
            }
            a[j + 1] = key;
            recordStep(a, i, j + 1, code + "\nInserted key " + key + " at position " + (j + 1));
        }
    }

    private void generateMergeSortSteps() {
        int[] a = array.clone();
        String code = "// Merge Sort Implementation\nmergeSort(arr, 0, n-1);";
        recordStep(a, -1, -1, code + "\n\nStarting Merge Sort...");
        mergeSort(a, 0, a.length - 1, code);
    }

    private void mergeSort(int[] a, int l, int r, String code) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(a, l, m, code);
            mergeSort(a, m + 1, r, code);
            merge(a, l, m, r, code);
        }
    }

    private void merge(int[] a, int l, int m, int r, String code) {
        int[] left = Arrays.copyOfRange(a, l, m + 1);
        int[] right = Arrays.copyOfRange(a, m + 1, r + 1);
        int i = 0, j = 0, k = l;
        while (i < left.length && j < right.length) {
            a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++];
            recordStep(a, l + i - 1, m + 1 + j - 1, code + "\nMerging subarrays...");
        }
        while (i < left.length) a[k++] = left[i++];
        while (j < right.length) a[k++] = right[j++];
    }

    // ===== SEARCHING ALGORITHMS =====
    private void generateLinearSearchSteps(int target) {
        int[] a = array.clone();
        String code = "// Linear Search Implementation\nfor (int i = 0; i < n; i++) {\n    if (arr[i] == target) return i;\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Linear Search...");

        for (int i = 0; i < a.length; i++) {
            recordStep(a, i, -1, code + "\nChecking index " + i + (a[i] == target ? ": Found target!" : ""));
            if (a[i] == target) break;
        }
    }

    private void generateBinarySearchSteps(int target) {
        int[] a = array.clone();
        Arrays.sort(a);
        String code = "// Binary Search Implementation\nint low=0,high=n-1;\nwhile(low<=high){\n    int mid=(low+high)/2;\n    if(arr[mid]==target)return mid;\n    else if(arr[mid]<target)low=mid+1;\n    else high=mid-1;\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Binary Search...");

        int low = 0, high = a.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            recordStep(a, mid, -1, code + "\nChecking mid index " + mid + (a[mid] == target ? ": Found target!" : ""));
            if (a[mid] == target) break;
            else if (a[mid] < target) low = mid + 1;
            else high = mid - 1;
        }
    }

    private void generateJumpSearchSteps(int target) {
        int[] a = array.clone();
        Arrays.sort(a);
        String code = "// Jump Search Implementation\nint step=(int)Math.sqrt(n),prev=0;\nwhile(arr[Math.min(step,n)-1]<target){prev=step;step+=Math.sqrt(n);}\nfor(int i=prev;i<Math.min(step,n);i++){if(arr[i]==target)return i;}";
        recordStep(a, -1, -1, code + "\n\nStarting Jump Search...");

        int n = a.length;
        int step = (int) Math.sqrt(n);
        int prev = 0;
        while (prev < n && a[Math.min(step, n) - 1] < target) {
            recordStep(a, Math.min(step, n) - 1, -1, code + "\nJumping to index " + (Math.min(step, n) - 1));
            prev = step;
            step += (int) Math.sqrt(n);
        }
        for (int i = prev; i < Math.min(step, n); i++) {
            recordStep(a, i, -1, code + "\nChecking index " + i + (a[i] == target ? ": Found target!" : ""));
            if (a[i] == target) break;
        }
    }

    private void generateInterpolationSearchSteps(int target) {
        int[] a = array.clone();
        Arrays.sort(a);
        String code = "// Interpolation Search Implementation\nint low=0,high=n-1;\nwhile(low<=high && target>=arr[low] && target<=arr[high]){\n    int pos=low+((target-arr[low])*(high-low))/(arr[high]-arr[low]);\n    if(arr[pos]==target)return pos;\n    if(arr[pos]<target)low=pos+1;\n    else high=pos-1;\n}";
        recordStep(a, -1, -1, code + "\n\nStarting Interpolation Search...");

        int low = 0, high = a.length - 1;
        while (low <= high && target >= a[low] && target <= a[high]) {
            int pos = low + ((target - a[low]) * (high - low)) / (a[high] - a[low]);
            recordStep(a, pos, -1, code + "\nChecking index " + pos + (a[pos] == target ? ": Found target!" : ""));
            if (a[pos] == target) break;
            if (a[pos] < target) low = pos + 1;
            else high = pos - 1;
        }
    }

    // ===== VISUAL PANEL =====
    private static class VisualPanel extends JPanel {
        private int[] array;
        private int h1, h2;

        void setArray(int[] arr, int h1, int h2) {
            this.array = (arr != null) ? arr.clone() : null;
            this.h1 = h1;
            this.h2 = h2;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (array == null || array.length == 0) return;

            int n = array.length;
            int w = getWidth();
            int h = getHeight();
            int barWidth = Math.max(10, w / n);
            int max = Arrays.stream(array).max().orElse(1);

            for (int i = 0; i < n; i++) {
                int val = array[i];
                int barH = (int)((val / (double)max) * (h - 60));
                int x = i * barWidth;
                int y = h - barH - 30;

                if (i == h1 || i == h2) g.setColor(new Color(255, 143, 0));
                else g.setColor(new Color(74, 92, 106));
                g.fillRect(x + 2, y, barWidth - 4, barH);
                g.setColor(Color.BLACK);
                g.drawRect(x + 2, y, barWidth - 4, barH);
                g.drawString(String.valueOf(val), x + (barWidth / 2) - 5, h - 10);
            }
        }
    }

    // ===== STEP CLASS =====
    private static class Step {
        int[] arr;
        int h1, h2;
        String description;

        Step(int[] arr, int h1, int h2, String description) {
            this.arr = arr;
            this.h1 = h1;
            this.h2 = h2;
            this.description = description;
        }
    }

    // ===== BUTTON STYLES =====
    private void stylePrimaryButton(JButton b) {
        b.setBackground(new Color(74, 92, 106));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private void styleSecondaryButton(JButton b) {
        b.setBackground(new Color(74, 92, 106));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VisualGo::new);
    }
}
