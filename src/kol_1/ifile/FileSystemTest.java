//package src.kol_1.ifile;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Scanner;
//
//class FileNameExistsException extends Exception {
//    public FileNameExistsException(String file, String folder) {
//        super(String.format("There is already a file named %s in the folder %s", file, folder));
//    }
//}
//
//interface IFile extends Comparable<IFile> {
//    String getFileName();
//
//    long getFileSize();
//
//    String getFileInfo(int indent);
//
//    void sortBySize();
//
//    long findLargestFile();
//
//    @Override
//    default int compareTo(IFile o) {
//        return Long.compare(getFileSize(), o.getFileSize());
//    }
//}
//
//class File implements IFile {
//    private String name;
//    private long size;
//
//    public File(String name, long size) {
//        this.name = name;
//        this.size = size;
//    }
//
//    @Override
//    public String getFileName() {
//        return name;
//    }
//
//    @Override
//    public long getFileSize() {
//        return size;
//    }
//
//    @Override
//    public String getFileInfo(int indent) {
//        String start = " ".repeat(indent);
//        return String.format("%sFile name: %10s File size: %10d\n", start, name, getFileSize());
//    }
//
//    @Override
//    public void sortBySize() {
//    }
//
//    @Override
//    public long findLargestFile() {
//        return size;
//    }
//}
//
//class Folder extends File implements IFile {
//
//    List<IFile> files;
//
//    public Folder(String name) {
//        super(name, 0);
//        this.files = new ArrayList<>();
//    }
//
//    @Override
//    public String getFileName() {
//        return super.getFileName();
//    }
//
//    @Override
//    public long getFileSize() {
//        return (long) files.stream().mapToDouble(it -> it.getFileSize()).sum();
//    }
//
//    @Override
//    public String getFileInfo(int indent) {
//        String start = " ".repeat(indent);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n", start, getFileName(), getFileSize()));
//
//        files.forEach(it -> sb.append(it.getFileInfo(indent + 4)));
//
//        return sb.toString();
//    }
//
//    @Override
//    public void sortBySize() {
//        files.sort(Comparator.naturalOrder());
//        files.forEach(it -> it.sortBySize());
//    }
//
//    @Override
//    public long findLargestFile() {
//        return files.stream().mapToLong(it -> it.findLargestFile()).max().orElse(0);
//    }
//
//    void addFile(IFile file) throws FileNameExistsException {
//        for (IFile it : files) {
//            if (it.getFileName().equals(file.getFileName())) {
//                throw new FileNameExistsException(file.getFileName(), getFileName());
//            }
//        }
//        files.add(file);
//    }
//
//}
//
//class FileSystem {
//    Folder rootDirectory;
//
//    public FileSystem() {
//        rootDirectory = new Folder("root");
//    }
//
//    void addFile(IFile file) throws FileNameExistsException {
//        rootDirectory.addFile(file);
//    }
//
//    long findLargestFile() {
//        return rootDirectory.findLargestFile();
//    }
//
//    void sortBySize() {
//        rootDirectory.sortBySize();
//    }
//
//    @Override
//    public String toString() {
//        return rootDirectory.getFileInfo(0);
//    }
//}
//
//public class FileSystemTest {
//
//    public static Folder readFolder(Scanner sc) {
//
//        Folder folder = new Folder(sc.nextLine());
//        int totalFiles = Integer.parseInt(sc.nextLine());
//
//        for (int i = 0; i < totalFiles; i++) {
//            String line = sc.nextLine();
//
//            if (line.startsWith("0")) {
//                String fileInfo = sc.nextLine();
//                String[] parts = fileInfo.split("\\s+");
//                try {
//                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
//                } catch (FileNameExistsException e) {
//                    System.out.println(e.getMessage());
//                }
//            } else {
//                try {
//                    folder.addFile(readFolder(sc));
//                } catch (FileNameExistsException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }
//
//        return folder;
//    }
//
//    public static void main(String[] args) {
//
//        //file reading from input
//
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("===READING FILES FROM INPUT===");
//        FileSystem fileSystem = new FileSystem();
//        try {
//            fileSystem.addFile(readFolder(sc));
//        } catch (FileNameExistsException e) {
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("===PRINTING FILE SYSTEM INFO===");
//        System.out.println(fileSystem.toString());
//
//        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
//        fileSystem.sortBySize();
//        System.out.println(fileSystem.toString());
//
//        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
//        System.out.println(fileSystem.findLargestFile());
//
//    }
//}