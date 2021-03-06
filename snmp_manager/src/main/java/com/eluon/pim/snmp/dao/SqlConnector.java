package com.eluon.pim.snmp.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlConnector {

	private static SqlSessionFactory factory;
	
	static {
		
		String resource = "mybatis-config.xml";
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			
			factory = new SqlSessionFactoryBuilder().build(reader);
		} catch(FileNotFoundException fileNotFoundException) { 
			fileNotFoundException.printStackTrace(); 
		} catch(IOException ioException) { 
			ioException.printStackTrace(); 
		} 
	}
	

	public static int insert(String mapper, Object param){
		int result = 0;
		SqlSession sqlSession = factory.openSession();
		result = sqlSession.insert(mapper, param);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	
	public static int update(String mapper, Object param){
		int result = 0;
		SqlSession sqlSession = factory.openSession();
		result = sqlSession.update(mapper, param);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	
	
	public static int delete(String mapper, Object param){
		int result = 0;
		SqlSession sqlSession = factory.openSession();
		result = sqlSession.delete(mapper, param);
		sqlSession.commit();
		sqlSession.close();
		return result;
	}
	
	public static <E> List<E> selectList(String mapper){
		return selectList(mapper, null);
	}
	
	public static <E> List<E> selectList(String mapper, Object param){
		SqlSession sqlSession = factory.openSession();
		List<E> result = sqlSession.selectList(mapper, param);
		sqlSession.close();
		return result;
	}
	
	public static <T> T selectOne(String mapper){
		return selectOne(mapper, null);
	}

	public static <T> T selectOne(String mapper, Object param){
		SqlSession sqlSession = factory.openSession();
		T result =  sqlSession.selectOne(mapper, param);
		sqlSession.close();
		return result;
	}
}