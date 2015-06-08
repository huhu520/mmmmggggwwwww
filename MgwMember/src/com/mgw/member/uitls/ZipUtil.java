package com.mgw.member.uitls;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;


/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @since 2011-10-12 ����9:51:57
 * @version 1.0.0
 */
public class ZipUtil {
//	private ZipFile zipFile;
//	private ZipOutputStream zipOut; // ѹ��Zip
//	private int bufSize; // size of bytes
//	private byte[] buf;
//	private int readedBytes;
//
//	public ZipUtil() {
//		this(512);
//	}
//
//	public ZipUtil(int bufSize) {
//		this.bufSize = bufSize;
//		this.buf = new byte[this.bufSize];
//	}
//
//	/**
//	 * 
//	 * @param srcFile
//	 *            ��Ҫ��ѹ����Ŀ¼�����ļ�
//	 * @param destFile
//	 *            ��ѹ���ļ���·��
//	 */
//	public void doZip(String srcFile, String destFile) {// zipDirectoryPath:��Ҫѹ�����ļ�����
//		File zipDir;
//		String dirName;
//
//		zipDir = new File(srcFile);
//		dirName = zipDir.getName();
//		try {
//			this.zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));
//			// ����ѹ����ע��
//			zipOut.setComment("comment");
//			// ����ѹ���ı��룬���Ҫѹ����·���������ģ���������ı���
//			zipOut.setEncoding("GBK");
//			// ����ѹ��
//			zipOut.setMethod(ZipOutputStream.DEFLATED);
//
//			// ѹ������Ϊ��ǿѹ������ʱ��Ҫ���ö�һ��
//			zipOut.setLevel(Deflater.BEST_COMPRESSION);
//
//			handleDir(zipDir, this.zipOut, dirName);
//			this.zipOut.close();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//	}
//
//	/**
//	 * ��doZip����,�ݹ����Ŀ¼�ļ���ȡ
//	 * 
//	 * @param dir
//	 * @param zipOut
//	 * @param dirName
//	 *            �����Ҫ��������¼ѹ���ļ���һ��Ŀ¼��νṹ��
//	 * @throws IOException
//	 */
//	private void handleDir(File dir, ZipOutputStream zipOut, String dirName) throws IOException {
//		System.out.println("����Ŀ¼��" + dir.getName());
//		FileInputStream fileIn;
//		File[] files;
//
//		files = dir.listFiles();
//
//		if (files.length == 0) {// ���Ŀ¼Ϊ��,�򵥶�����֮.
//			// ZipEntry��isDirectory()������,Ŀ¼��"/"��β.
//			System.out.println("ѹ���ġ�Name:" + dirName);
//			this.zipOut.putNextEntry(new ZipEntry(dirName));
//			this.zipOut.closeEntry();
//		} else {// ���Ŀ¼��Ϊ��,��ֱ���Ŀ¼���ļ�.
//			for (File fileName : files) {
//				// System.out.println(fileName);
//
//				if (fileName.isDirectory()) {
//					handleDir(fileName, this.zipOut, dirName + File.separator + fileName.getName() + File.separator);
//				} else {
//					System.out.println("ѹ���ġ�Name:" + dirName + File.separator + fileName.getName());
//					fileIn = new FileInputStream(fileName);
//					this.zipOut.putNextEntry(new ZipEntry(dirName + File.separator + fileName.getName()));
//
//					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
//						this.zipOut.write(this.buf, 0, this.readedBytes);
//					}
//
//					this.zipOut.closeEntry();
//				}
//			}
//		}
//	}
//
//	/**
//	 * ��ѹָ��zip�ļ�
//	 * 
//	 * @param unZipfile
//	 *            ѹ���ļ���·��
//	 * @param destFile
//	 *            ��������ѹ����Ŀ¼��
//	 */
//	public void unZip(String unZipfile, String destFile) {// unZipfileName��Ҫ��ѹ��zip�ļ���
//		FileOutputStream fileOut;
//		File file;
//		InputStream inputStream;
//
//		try {
//			this.zipFile = new ZipFile(unZipfile);
//
//			for (Enumeration entries = this.zipFile.getEntries(); entries.hasMoreElements();) {
//				ZipEntry entry = (ZipEntry) entries.nextElement();
//				file = new File(destFile + File.separator + entry.getName());
//
//				if (entry.isDirectory()) {
//					file.mkdirs();
//				} else {
//					// ���ָ���ļ���Ŀ¼������,�򴴽�֮.
//					File parent = file.getParentFile();
//					if (!parent.exists()) {
//						parent.mkdirs();
//					}
//
//					inputStream = zipFile.getInputStream(entry);
//
//					fileOut = new FileOutputStream(file);
//					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
//						fileOut.write(this.buf, 0, this.readedBytes);
//					}
//					fileOut.close();
//
//					inputStream.close();
//				}
//			}
//			this.zipFile.close();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//	}
//
//	// ���û������С
//	public void setBufSize(int bufSize) {
//		this.bufSize = bufSize;
//	}
}
