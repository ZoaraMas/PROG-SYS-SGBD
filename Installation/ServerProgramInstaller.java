package Installation;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class ServerProgramInstaller {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!isRunningAsAdmin()) {
            System.err.println("This program must be run as an administrator.");
            System.exit(1);
        }

        try {
            // Step 1: Ask for the port number
            System.out.print("Enter the port number for the server: ");
            String port = scanner.nextLine();

            // Step 2: Ask for the directory to save files
            System.out.print("Enter the directory to save the server files: ");
            String saveDirPath = scanner.nextLine();
            saveDirPath = saveDirPath.replaceAll("\\\\", "/");

            // Write the port to a text file
            // Write the path
            File conf = new File("SGBD/Conf.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(conf))) {
                writer.write("port=" + port + "\n");
                writer.write("path=" + saveDirPath + "/save/db.ser\n");
                // temp line first (proper to program)
                writer.write("temp" + "\n");
                writer.write("path=" + saveDirPath + "/save/dbRep.ser\n");
                // System.out.println("Port number saved to: " + conf.getAbsolutePath());
            }

            File saveDir = new File(saveDirPath, "save");
            // Create the 'save' directory
            if (!saveDir.exists()) {
                saveDir.mkdirs();
                // System.out.println("Created directory: " + saveDir.getAbsolutePath());
            }

            // copye the ser files
            Path sourceFile1 = Paths.get("SGBD/save/db.ser");
            Path sourceFile2 = Paths.get("SGBD/save/dbRep.ser");
            // Define target files in the 'save' directory
            Path targetFile1 = Paths.get(saveDir.getAbsolutePath(), "db.ser");
            Path targetFile2 = Paths.get(saveDir.getAbsolutePath(), "dbRep.ser");
            // Copy files to the target directory
            Files.copy(sourceFile1, targetFile1, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourceFile2, targetFile2, StandardCopyOption.REPLACE_EXISTING);

            // System.out.println("Files copied to: " + saveDir.getAbsolutePath());

            // Step 3: Ask for the directory to create the server folder
            System.out.print("Enter the directory where the server folder should be created: ");
            String serverDirPath = scanner.nextLine();
            File serverDir = new File(serverDirPath, "server");

            // Create the server folder
            if (!serverDir.exists()) {
                serverDir.mkdirs();
                // System.out.println("Created server directory: " +
                // serverDir.getAbsolutePath());
            }

            // Copy port.txt and save directory to the server folder
            Files.copy(conf.toPath(), new File(serverDir, "Conf.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
            // System.out.println("The directory has been copied");

            File actualServer = new File("SGBD");
            Path sourceSaveDir = actualServer.toPath();
            Path targetSaveDir = serverDir.toPath();

            copyDirectory(sourceSaveDir, targetSaveDir);
            // System.out.println("Server folder prepared successfully at: " +
            // serverDir.getAbsolutePath());

            // Make the server a process
            // System.out.println("instal the server");
            executeCommand("nssm install " + "MySgbd" + " \"" + serverDir.getAbsolutePath() + "\\launch.exe\"");
            // System.out.println("make it auto start");
            executeCommand("nssm set " + "MySgbd" + " Start SERVICE_AUTO_START");
            // System.out.println("restart it");
            executeCommand("nssm restart " + "MySgbd");

            System.out.println("Service " + "MySgbd" + " installed and started successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }

    }

    // Utility method to copy a directory and its contents
    private static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
        Files.walk(sourceDir).forEach(sourcePath -> {
            try {
                Path targetPath = targetDir.resolve(sourceDir.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectory(targetPath);
                    }
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    // private static void executeCommand(String command) {
    // try {
    // String[] elevatedCommand = { "cmd.exe", "/c", "runas", "/user:Administrator",
    // command };
    // Process process = new ProcessBuilder(elevatedCommand).start();
    // process.waitFor();

    // if (process.exitValue() != 0) {
    // try (BufferedReader reader = new BufferedReader(new
    // InputStreamReader(process.getErrorStream()))) {
    // String line;
    // while ((line = reader.readLine()) != null) {
    // System.err.println(line);
    // }
    // }
    // }
    // } catch (IOException | InterruptedException e) {
    // System.err.println("Failed to execute command: " + command);
    // e.printStackTrace();
    // }
    // }

    private static boolean isRunningAsAdmin() {
        try {
            Process process = Runtime.getRuntime().exec("net session");
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    // C:\Users\Titix\Desktop\SGBD

    // C:\MyServer2

    private static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if (process.exitValue() != 0) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to execute command: " + command);
            e.printStackTrace();
        }
    }
}
