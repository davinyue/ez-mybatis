package org.linuxprobe.crud.mybatis.session;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class UniversalCrudSqlSessionFactoryBuilder extends SqlSessionFactoryBuilder{
	public void test(){
		SqlSessionFactory sessionFactory = null;
		SqlSession sqlSession =sessionFactory.openSession();
		sqlSession.insert("");
		//super
	}
}
