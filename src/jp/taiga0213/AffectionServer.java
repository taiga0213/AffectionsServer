package jp.taiga0213;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.arnx.jsonic.JSON;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class AffectionServer
 */
@WebServlet("/AffectionServer")
public class AffectionServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AffectionServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println("GET");

		List<AffectionBean> result = null;
		try {
			AffectionDao dao = new AffectionDao();
			result = dao.findAll();
			dao.close();
		} catch (NamingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		String image = JSON.encode(result);

		out.println(image);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		System.out.println("POST");

		if (ServletFileUpload.isMultipartContent(request)) {

			// ファクトリー生成
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1426);
			// factory.setRepository(new java.io.File("C:\\temp\\Affections"));
			// // 一時的に保存する際のディレクトリ

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(40 * 1024);
			upload.setFileSizeMax(40 * 1024);

			List<FileItem> items;
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e) {
				// エラー処理
				throw new ServletException(e);
			}

			// 全フィールドに対するループ

			AffectionBean affectionBean = new AffectionBean();
			for (Object val : items) {
				FileItem item = (FileItem) val;
				if (item.isFormField()) {
					// type="file"以外のフィールド
					processFormField(item);

					switch (item.getFieldName()) {
					case "app_name":
						affectionBean.setAppName(item.getString("UTF-8"));
						break;
					case "app_package":
						affectionBean.setAppPackage(item.getString("UTF-8"));
						break;
					case "affections":
						affectionBean.setAffections(item.getString("UTF-8"));
						break;
					}

				} else {
					// type="file"のフィールド
					processUploadedFile(item);
				}
			}

			if (affectionBean.getAppName() != null
					&& affectionBean.getAppPackage() != null
					&& affectionBean.getAffections() != null) {

				try {
					AffectionDao dao = new AffectionDao();
					System.out.println(dao.insert(affectionBean));
					dao.commit();
					dao.close();
				} catch (NamingException | SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

			}

			/* 出力する画像ファイル名 */
			String fileName = "C:\\TEMP\\Affections\\jp.taiga.cloudmashup.png";
			byte[] fileBinary = readBinaryFile(fileName);
			AffectionBean bean = new AffectionBean();
			bean.setAppIcon(fileBinary);
			String image = JSON.encode(bean);

			out.println(image);
		}
	}

	private void processUploadedFile(FileItem item) throws IOException,
			ServletException {
		java.io.File f = new java.io.File(item.getName());
		try {
			item.write(new java.io.File("C:\\TEMP\\Affections", f.getName()));
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void processFormField(FileItem item) throws ServletException {

		try {
			System.out.println(item.getFieldName() + "+"
					+ item.getString("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * フルパスのファイル名のファイルをbyte[]に変換する
	 *
	 * @param fileName
	 *            String
	 * @return byte[]
	 * @throws IOException
	 */
	private static byte[] readBinaryFile(String fileName) throws IOException {
		File file = new File(fileName);
		int fileLength = (int) file.length();
		byte[] bytes = new byte[fileLength];
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			inputStream.read(bytes);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return bytes;
	}

}
