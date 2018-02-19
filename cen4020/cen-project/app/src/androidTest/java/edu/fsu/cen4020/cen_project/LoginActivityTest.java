package edu.fsu.cen4020.cen_project;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by victor on 2/18/18.
 */
public class LoginActivityTest {

    // Place tests here
    // For testing username and password pre-validation requirements

    // Test Cases Pair Programming by Victor & Phalguna

    // Pass:
    public String email_test_pass = "victor@google.com";    // pass, contains '@' and '.'
    public String password_test_pass = "testpassword";      // pass, length > 5

    // Fail:
    public String email_test_fail = "victor'at'msn.com";    // no pass
    public String password_test_fail = "bob";               // no pass

    @Test
    public void check_isEmailValid() throws Exception {
        assertTrue(LoginActivity.isEmailValid(email_test_fail));
    }

    @Test
    public void check_isPasswordValid() throws Exception {
        assertTrue(LoginActivity.isPasswordValid(password_test_fail));
    }

}