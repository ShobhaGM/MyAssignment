package com.temp.assign;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Shobha
 *
 */
public class VirtualFileSystem {

	/*
	 * Create a progrm that will mimic a file system in a non-persistent way.
	 * Your program should not write anything to the HDDs and when it is
	 * restarted, the "file system" should be empty. Your program should handle
	 * the following file system commands: Create a new folder - Takes a
	 * parameter of absolute folder path Create a new file - Take a parameter of
	 * absolute file path Add content to a file - Take 2 parameters: Content to
	 * append to a file; Absolute path to a file Copy files - Takes 2
	 * parameters: Absolute path to a source file; Absolute path to a
	 * destination file (NOTE: If destination file exists, it will be
	 * overwritten) Display file contents - Takes absolute path to a file as an
	 * input; Prints out file contents as an output List folder contents - Takes
	 * absolute path to a folder as an input; Prints out folder contents as an
	 * output Search for a file by name - Takes name of a file to find; Prints
	 * out list of absolute paths to files with matching names Search for a file
	 * by name - Takes 2 parameters: Absolute path to a starting folder and file
	 * name; Outputs list of absolute paths to files with matching names (Bonus)
	 * Copy folders - Takes 2 parameters: Absolute path to source folder,
	 * Absolute path to destination folder
	 */

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		boolean bContinue = true;
		while (bContinue) {
			System.out.println(
					"\n Options available \n" 
							+ "1. mkdir /someFolder \n" 
							+ "2.create /file1  \n"
							+ "3.create /someFolder/file1 \n" 
							+ "4.write data /file1 \n" 
							+ "5.cat /file1 \n"
							+ "6.cp /file1 /someFolder/file2 \n"
							+ "7.find file2 (Should return all found locations for file2) \n" 
							+ "8.find /someFolder file2 \n"
							+ "9.ls /someFolder \n" 
							+ "10.(Bonus) cp /someFolder /copyFolder \n" 
							+ "11. exit   \n" 
							+ "Enter a number :: ");

			int operation = scan.nextInt();

			switch (operation) {
			case 1: {
				System.out.println("Enter absolute path to create new folder(non persistent one)");
				String s = scan.next();
				mkdir(s);
			}
				break;
			case 2: {
				System.out.println("\n Enter absolute path to create new temp file \n");
				String filepath = scan.next();
				create(filepath);
			}
				break;
			case 3: {
				System.out.println(" Enter absolute path to create new file along with its folder structure");
				String fp = scan.next();
				create(fp);
			}
				break;
			case 4: {
				System.out.println("Enter absolute path of a file to write");
				String filepath = scan.next();
				System.out.println("\n Enter data to be written to output file");
				Scanner ss = scan.useDelimiter("\\n");
				String data = ss.next();
				System.out.println("input data: " + data);
				// String data = "This is the data written to the file -
				// outpu.txt!!!";
				write(filepath, data);
			}
				break;
			case 5: {
				System.out.println("Enter absolute path of a file whose contents to be displayed");
				String filepath = scan.next();
				cat(filepath);
			}
				break;
			case 6: {
				System.out.println("\n To Copy source file content to dest file - enter path names");
				System.out.println("Enter absolute path of source file");
				String sfilepath = scan.next();
				System.out.println("Enter absolute path of dest file");
				String dfilepath = scan.next();
				cp(sfilepath, dfilepath);
			}
				break;
			case 7: {
				System.out.println("\n \n Enter a filename to be searched under root dir ");
				String filename = scan.next();
				find(filename);
			}
				break;
			case 8: {
				System.out.println("\n \n Enter a filename to find its path under specified dir ");
				String fName = scan.next();
				System.out.println("\n Enter starting folder name");
				String folder = scan.next();
				find(folder, fName);
			}
				break;
			case 9: {
				System.out.println("\n \n Enter a dir whose contents to be listed");
				String dirpath = scan.next();
				ls(dirpath);
			}
				break;
			case 10: {
				System.out.println("\n To copy enter a source folder \n ");
				String sfName = scan.next();
				System.out.println("\n To copy enter a dest folder \n ");
				String dfName = scan.next();

				File srcFolder = new File(sfName);
				File destFolder = new File(dfName);

				if (!srcFolder.exists()) {
					System.out.println("\n Directory doesn't exist");
				} else {
					try {
						cp(srcFolder, destFolder);
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
			break;

			case 11: {
				bContinue = false;
				break;
			}

			}
		}
		scan.close();
	}

	/*
	 * 1. mkdir /someFolder Create a new folder - Takes a parameter of absolute folder path
	 * @param absoluteFolderPath:path in which a new folder is created
	 * @return File: an abstract pathname for the newly created dir
	 */
	public static void mkdir(String absoluteFolderPath) {

		File dir = new File(absoluteFolderPath);

		if (dir.getParentFile() != null && !dir.getParentFile().exists()) {
			String parentDir = dir.getParent();
			System.out.println("Calling to create parent dir: " + parentDir);
			mkdir(parentDir);
		}

		if (dir.getParentFile() != null && !dir.exists()) {
			boolean successful = dir.mkdir();
			if (successful) {
				dir.deleteOnExit();
				System.out.println("Successfully created Directory: " + absoluteFolderPath);
			} else {
				System.out.println("Failed to create the directory: " + absoluteFolderPath);
			}
		} else {
			System.out.println(" Dir already exists !!");
		}
	}

	/*
	 * create /someFolder/file1 Create a new file - Take a parameter of absolute
	 * file path - serves 2 purpose if file doesn't exist creates one or creates
	 * dir structure along with new file
	 */
	public static File create(String absoluteFilePath) {

		File file = new File(absoluteFilePath);

		try {
			if (!file.exists()) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					System.out.println("Calling to create Parent dir: " + file.getParent());
					mkdir(file.getParent());
				}
				file.createNewFile();
				file.deleteOnExit();
				System.out.println("Successfully created File: " + absoluteFilePath);
			} else {
				System.out.println(" File already exists !!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/*
	 * 4. write "Some text" /file1 Add content to a file - Take 2 parameters:
	 * Content to append to a file; Absolute path to a file
	 * 
	 * @param absoluteFilePath: absolute path of the file to be written
	 * 
	 * @param data: text to be written
	 * 
	 */
	public static void write(String absoluteFilePath, String data) {

		File file = create(absoluteFilePath);

		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();

			System.out.println("data written to file");
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/*
	 * 5.Display file contents - Takes absolute path to a file as an input;
	 * Prints out file contents as an output
	 */
	public static void cat(String absoluteFilePath) {

		File file = new File(absoluteFilePath);
		if (file.exists()) {
			FileInputStream fs = null;

			System.out.println("Reading contents from file ------");
			try {
				fs = new FileInputStream(file);
				int content;
				while ((content = fs.read()) != -1) {
					System.out.print((char) content);
				}

				System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (fs != null)
						fs.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	/*
	 * Copy files - Takes 2 parameters: Absolute path to a source file; Absolute
	 * path to a destination file (NOTE: If destination file exists, it will be
	 * overwritten)
	 * @param filepath1
	 * @param filepath2
	 */
	public static void cp(String srcPath, String destPath) {

		File ip = new File(srcPath);
		File op = new File(destPath);

		if (ip.exists()) {
			if (!op.exists()) {
				op = create(destPath);
			}

			FileReader fr = null;
			FileWriter fw = null;

			try {
				fr = new FileReader(ip);
				fw = new FileWriter(op, false);

				int c = fr.read();
				while (c != -1) {
					fw.write(c);
					c = fr.read();
				}
				System.out.println("Successfully copied file src->dest : " + srcPath + " -> " + destPath);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fr.close();
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		} else {
			System.out.println("Source File does not exists!!!");
		}

	}

	/*
	 * List folder contents - Takes absolute path to a folder as an input;
	 * Prints out folder contents as an output
	 * @param fp: absolute file path afp
	 */
	public static void ls(String afp) {

		File f = new File(afp);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File file : files) {
				try {
					System.out.println(file.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			System.out.println("File is not a directory but a simple file ");
		}
	}
	/*
	 * Search for a file by name - Takes name of a file to find; Prints out list
	 * of absolute paths to files with matching names
	 * 
	 * 
	 */

	public static void find(String searchFile) {
		String ROOT = "/Users/shobha";
		find(ROOT, searchFile);
	}

	/*
	 * Search for a file by name - Takes 2 parameters: Absolute path to a
	 * starting folder and file name; Outputs list of absolute paths to files
	 * with matching names
	 * 
	 */
	public static void find(String dir, String searchFile) {
		// System.out.println("dir: "+ dir);
		// System.out.println("file: "+ searchFile);

		File dirPath = new File(dir);

		if (dirPath.isDirectory()) {
			// check wheather permission to read this dir
			if (dirPath.canRead()) {
				for (File temp : dirPath.listFiles()) {
					if (!temp.isDirectory()) {
						// System.out.println("temp: " + temp.getName());
						// File Object comparison
						if (temp.equals(new File(dir + "/" + searchFile))) {
							System.out.println("File-Object found at: " + temp.getAbsolutePath());
						}

					} else {
						// System.out.println("--- dir: " +
						// temp.getAbsolutePath());
						find(temp.getAbsolutePath(), searchFile);
					}
				}
			} else {
				System.out.println(dirPath.getAbsolutePath() + ":  Permission Denied");
			}

		}
	}

	/*
	 * (Bonus) Copy folders - Takes 2 parameters: Absolute path to source
	 * folder, Absolute path to destination folder (Bonus) cp /someFolder
	 * /copyFolder Takes 2 parameters: Absolute path to source folder, Absolute
	 * path to destination folder
	 * 
	 */

	public static void cp(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				System.out.println("Calling to create dest dir: " + dest.getAbsolutePath());
				mkdir(dest.getAbsolutePath());
			}
			String files[] = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);

				// dir copy
				System.out.println("Copying src -> dest:: " + srcFile.getAbsolutePath() + "  ->  " + destFile.getAbsolutePath());
				if (srcFile.isDirectory()) {
					cp(srcFile, destFile);
				} else {
					cp(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
				}
			}

		} else {
			// File copy
			cp(src.getAbsolutePath(), dest.getAbsolutePath());
		}

	}
}
