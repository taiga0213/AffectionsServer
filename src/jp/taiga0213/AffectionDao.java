package jp.taiga0213;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

public class AffectionDao {

	private Connection con;

	/**
	 * コンストラクタ
	 *
	 * @throws NamingException
	 * @throws SQLException
	 */
	public AffectionDao() throws NamingException, SQLException {
		ConnectionGet get = new ConnectionGet();
		con = get.getCon();
	}

	/**
	 * 全件取得する
	 *
	 * @return 全件
	 * @throws SQLException
	 * @throws IOException
	 */
	public List<AffectionBean> findAll() throws SQLException, IOException {

		PreparedStatement select = con
				.prepareStatement("select * from affections");

		ResultSet result = select.executeQuery();

		ArrayList<AffectionBean> table = new ArrayList<AffectionBean>();
		while (result.next()) {

			AffectionBean record = new AffectionBean();

			record.setId(result.getInt("id"));
			record.setAppName(result.getString("app_name"));
			record.setAppPackage(result.getString("app_package"));
			record.setAffections(result.getString("affections"));
			record.setDate(result.getDate("date"));

			String fileName = "C:\\TEMP\\Affections\\"+result.getString("app_package")+".png";
			byte[] image = readBinaryFile(fileName);

			record.setAppIcon(image);

			table.add(record);
		}

		return table;
	}

	/**
	 * 新規保存
	 *
	 * @param newRecord 保存データ
	 * @return 影響のあった行数
	 * @throws SQLException
	 */
	public int insert(AffectionBean newRecord) throws SQLException {

		PreparedStatement insert = con.prepareStatement("insert into affections (app_name,app_package,affections,date) values (?,?,?,NOW())");
		insert.setString(1, newRecord.getAppName());
		insert.setString(2, newRecord.getAppPackage());
		insert.setString(3, newRecord.getAffections());

		return insert.executeUpdate();
	}

	/**
	 * 接続を閉じる
	 *
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		con.close();
	}

	/**
	 * コミット
	 *
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		con.commit();
	}

	/**
	 * ロールバック
	 *
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		con.rollback();
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
