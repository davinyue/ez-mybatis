package test;
import java.io.IOException;
import java.util.List;

import org.linuxprobe.crud.utils.ClassScan;

public class ScanTest {

	public static void main(String[] args) throws IOException {
		List<Class<?>>  clazzs = ClassScan.scan("org.linuxprobe");
		for(Class<?> clazz:clazzs){
			System.out.println(clazz.getName());
		}
		
	}
}
