package com.igz.manager;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;
import com.igz.exception.IgzException;
import com.igz.test.helper.ExceptionMatcher;
import com.igz.test.helper.TestHelper;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class UserManagerTest extends TestCase {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			 new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(1)
    		,new LocalBlobstoreServiceTestConfig());

    private final UserManager userM = new UserManager();

    private static final String USER_EMAIL = "usermanager@intelygenz.com";
    private static final String USER_NAME = "Test username";
    private static final String USER_NAME_UPDATED = "Modified Test username";

    @Rule
    public ExpectedException thrown = ExpectedException.none();  
    
    @Override
    @Before
    public void setUp() {
        this.helper.setUp();
        TestHelper.prepareDS();
    }

    @Override
    @After
    public void tearDown() {
        this.helper.tearDown();
    }

    @Test
    public void testLoginNullEmail() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode( IgzException.IGZ_ERR_IN_LOGIN));
        
    	UserDto user = new UserDto();
    	userM.loginUser( null, user);
    }

    @Test
    public void testLoginEmptyEmail() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode( IgzException.IGZ_ERR_IN_LOGIN));
        
    	UserDto user = new UserDto();
    	userM.loginUser( "", user);
    }

    @Test
    public void testLoginBadEmail() throws IgzException {
    	thrown.expect(IgzException.class);
    	thrown.expect(ExceptionMatcher.hasCode( IgzException.IGZ_ERR_IN_LOGIN));
        
    	UserDto user = new UserDto();
    	userM.loginUser("badmail.com", user);
    }

    @Test
    public void testFirstLogin() throws IgzException {
    	UserDto user = new UserDto();
    	user = userM.loginUser( USER_EMAIL, user);

    	assertEquals( user.getEmail(), USER_EMAIL);
    	
    	UserDto userFromDS = userM.get( USER_EMAIL );
    	assertNotNull( userFromDS );
    }
    
    @Test
    public void testFirstLoginWithDomainData() throws IgzException {
    	UserDto user = new UserDto();
    	user.setFullname( USER_NAME );
    	user = userM.loginUser( USER_EMAIL, user);

    	assertEquals( user.getFullname(), USER_NAME);
    	
    	UserDto userFromDS = userM.get( USER_EMAIL );
    	assertNotNull( userFromDS );

    	assertEquals( USER_NAME, userFromDS.getFullname() );
    }

    @Test
    public void testUpdateUserWithDomainDataInLogin() throws IgzException {
    	//first login
    	UserDto user = new UserDto();
    	user.setFullname( USER_NAME );
    	userM.loginUser( USER_EMAIL, user);

    	UserDto updatedUser = new UserDto();
    	updatedUser.setFullname( USER_NAME_UPDATED );
    	userM.loginUser( USER_EMAIL, updatedUser);

    	UserDto userFromDS = userM.get( USER_EMAIL );
    	assertNotNull( userFromDS );

    	assertEquals( USER_NAME_UPDATED, userFromDS.getFullname());
    }
    
}
