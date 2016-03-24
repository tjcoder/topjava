package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.*;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceTest
{
    @Autowired
    private UserMealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception
    {
        UserMeal userMeal = service.get(1, USER_ID);
        assertEquals(Integer.valueOf(1), userMeal.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception
    {
        service.get(1, ADMIN_ID);
    }

    @Test
    public void testDelete() throws Exception
    {
        service.delete(1, USER_ID);
        assertEquals(service.getAll(USER_ID).size(), 2);
    }

    @Test
    public void testGetBetweenDates() throws Exception
    {

    }

    @Test
    public void testGetBetweenDateTimes() throws Exception
    {

    }

    @Test
    public void testGetAll() throws Exception
    {
        assertEquals(service.getAll(USER_ID).size(), 3);
    }

    @Test
    public void testUpdate() throws Exception
    {

    }

    @Test
    public void testSave() throws Exception
    {

    }
}