package com.nexio;

import com.nexio.domain.Customer;
import com.nexio.domain.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class ApplicationTests {

	@Autowired
	private CustomerRepository customerRepository;

//	@Autowired
//	private CacheManager cacheManager;

	@Before
	public void before() {
		customerRepository.save(new Customer("AAA", 10));
	}

	@Test
	public void test() throws Exception {

		Customer u1 = customerRepository.findByName("AAA");
		System.out.println("第一次查询：" + u1.getAge());

		Customer u2 = customerRepository.findByName("AAA");
		System.out.println("第二次查询：" + u2.getAge());

		u1.setAge(20);
		customerRepository.save(u1);
		Customer u3 = customerRepository.findByName("AAA");
		System.out.println("第三次查询：" + u3.getAge());

	}

}
