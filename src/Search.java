import java.io.*;
import java.util.*;

public class Search {

    private static BufferedReader _reader;
    private static String _srcDirPath;
    private static Search _search;

    public static void main(String[] args) {
        _search = new Search();
        _search.start();
    }

    public void start() {
        try {

            // Enter data using BufferReader
            _reader = new BufferedReader(
                    new InputStreamReader(System.in));

            System.out.println(" Enter directory path: ");

            // Reading data using readLine
            _srcDirPath = checkExitCommand(_reader.readLine());
            _srcDirPath = _srcDirPath.endsWith("/") ? _srcDirPath : _srcDirPath + "/";

            File folder = new File(_srcDirPath);

            if (!folder.exists() || !folder.canRead() || folder.isFile()) {
                System.out.println(" The entered directory path does not exists or can not read... ");
                _search.start();
            } else
                readWordListToSearch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readWordListToSearch() {
        try {
            List<String> keywords = new ArrayList<>();

            System.out.println(" Enter search words seperated by a comma ',' : ");

            // Reading data using readLine
            String worlds = checkExitCommand(_reader.readLine()).trim();

            if (worlds.isEmpty())
                readWordListToSearch();
            else if (!worlds.contains(",")) {
                keywords = new ArrayList<String>(Arrays.asList(worlds));
            } else
                keywords = Arrays.asList(worlds.split("\\s*,\\s*"));

            _search = new Search();
            _search.search(_srcDirPath, keywords);

        } catch (Exception e) {
            System.out.println("Please check your word list and try again... \n Enter search words seperated by a comma ',' : ");
            readWordListToSearch();
        }

    }

    public void search(String srcDir, List<String> keywords) {

        try {
            HashMap<String, Integer> keywordsCount = new HashMap<String, Integer>();

            File folder = new File(srcDir);
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles.length > 0) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        Scanner a = null;
                        // this is to avoid reading .DS_Store file in OSX
                        if (!listOfFiles[i].getName().endsWith("DS_Store")) {

                            a = new Scanner(new BufferedReader(new FileReader(srcDir + listOfFiles[i].getName())));
                            while (a.hasNext()) {
                                String words = a.next();
                                for (String keyword : keywords) {
                                    if (words.equalsIgnoreCase(keyword)) {

                                        if (keywordsCount.containsKey(keyword)) {
                                            keywordsCount.replace(keyword, keywordsCount.get(keyword) + 1);
                                        } else {
                                            keywordsCount.put(keyword, 1);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }

            keywordsCount.entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + " " + entry.getValue());
            });

            checkUserWantsToSearchAgain();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String checkExitCommand(String exitCommand) {

        if (exitCommand.equalsIgnoreCase("exit;")) {
            System.out.println("Thank you for using string search...");
            System.exit(1);
        } else
            return exitCommand;

        return null;
    }

    public static boolean checkUserWantsToSearchAgain() throws IOException {

        System.out.println("Do you want to search again ? \nIf YES enter Y or if you wish to exit type N :");

        String command = _reader.readLine();

        if (command.equalsIgnoreCase("y"))
            readWordListToSearch();
        else if (command.equalsIgnoreCase("n"))
            checkExitCommand("exit;");

        return false;
    }
}
