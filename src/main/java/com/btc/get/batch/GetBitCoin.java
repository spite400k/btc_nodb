package com.btc.get.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBitCoin {

	private static String ENDPOINTURI = "https://api.bitflyer.jp";

	private static Logger logger = LoggerFactory.getLogger(GetBitCoin.class);
	private static Integer SLEEP_TIME = 5 * 1000; // １処理あたりの休眠時間 秒
	private static Integer RUN_TIME = 20; // 指定の秒の間、実行

	public GetBitCoin() {
	}

	public static void main(String[] args) {

		try {
			get();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	/**
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void get() throws IOException, InterruptedException {
		String jsonData = "";
		boolean continuos = true;

		Date nowDate1 = new Date();
		Date nowDate2 = new Date();

		while (continuos) {
			jsonData = getHttpRequest(ENDPOINTURI, "/v1/board?product_code=BTC_JPY");

			logger.info(jsonData);

			nowDate2 = new Date();
			if (nowDate2.after(DateUtils.addSeconds(nowDate1, RUN_TIME))) {
				continuos = false;
			}
			Thread.sleep(SLEEP_TIME);
		}
	}

	/**
	 *
	 * @param site_url
	 * @param pass_url
	 * @return
	 * @throws IOException
	 */
	public static String getHttpRequest(String site_url, String pass_url) throws IOException {

		URL url = new URL(site_url + pass_url);

		HttpURLConnection connection = null;

		try {
			// コネクションを作成
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setUseCaches(true);

			String getStr = "";

			// レスポンスを取得
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// 文字列として出力
				try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

						BufferedReader reader = new BufferedReader(isr)) {
					String line;
					while ((line = reader.readLine()) != null) {
						getStr += line;
					}
				}
			} else {
				// エラー処理(エラーの場合はnullを返す)
				try (InputStreamReader isr = new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8);

						BufferedReader reader = new BufferedReader(isr)) {
					String line;
					while ((line = reader.readLine()) != null) {
						getStr += line;
					}
				}
				return null;
			}

			return getStr;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			// return null;
		}
	}

}
