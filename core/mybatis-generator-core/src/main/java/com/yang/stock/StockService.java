package com.yang.stock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yang.core.database.ObjectConnection;
import com.yang.stock.bean.Stock;

public class StockService {

	private StockDao dao=new StockDao();
	private ObjectConnection connection;
	
	public StockService(String code) {
		super();
//		this.code=code;
		connection=new ObjectConnection(code);
	}
	public void insert() throws SQLException{
		Object object = connection.getFromURL();
		if(object instanceof List){
			@SuppressWarnings("unchecked")
			List<Stock> list=(List<Stock>) object;
			List<Stock> lis=new ArrayList<Stock>();
			for (Stock stocks : list) {
				int i = dao.count(stocks);
				if(i==0){
					lis.add(stocks);
				}
			}
			dao.batchInsert(lis);
		}
	}
}
