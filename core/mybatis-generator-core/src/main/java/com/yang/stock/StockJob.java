package com.yang.stock;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.JAXBException;

public class StockJob {

	public void startJob(String code) {
		Timer timer = new Timer();
		TimerTask task = new MyTimerTask(code);
		timer.schedule(task, 100, 5000);
	}

	class MyTimerTask extends TimerTask {

		private StockService service;

		public MyTimerTask(String code) {
			super();
//			this.code=code;
			service = new StockService(code);
		}

		@Override
		public void run() {
			System.err.println("job running.....");
			Calendar ca = Calendar.getInstance();
			int h = ca.get(Calendar.HOUR_OF_DAY);
			int m = ca.get(Calendar.MINUTE);
			int s = ca.get(Calendar.SECOND);
			int w = ca.get(Calendar.DAY_OF_WEEK);
			System.err.println(h+"--"+m+w+s);
			boolean run = false;
			if (h == 9 && m > 29) {
				run = true;
			} else if (h > 9 && h < 15) {
				run = true;
			}
			if (w == Calendar.SUNDAY || w == Calendar.SATURDAY) {
				run = false;
			}
			try {
				if (run) {
					service.insert();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public static void main(String[] args) throws JAXBException, InterruptedException {
		List<A> list = new StockCode().getAllCode();
		for (A a : list) {
			new StockJob().startJob(a.getHref().split(",")[1]);
		}
	}
}
