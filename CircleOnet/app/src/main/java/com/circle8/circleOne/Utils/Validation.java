package com.circle8.circleOne.Utils;

import android.text.Html;
import android.view.View;

import com.circle8.circleOne.Activity.LoginActivity;
import com.circle8.circleOne.Activity.MyAccountActivity;
import com.circle8.circleOne.Activity.RegisterActivity;

/**
 * Created by ample-arch on 7/28/2017.
 */

public class Validation
{
    public static boolean validate(String userName,String firstName, String lastName, String password, String rePassword, String contactNo, String emailAddress)
    {
        boolean valid = true ;

        if(userName.isEmpty() || userName.length() < 1 )
        {
//            RegisterActivity.etUserName.setError("Minimum 3 Characters.");
            RegisterActivity.tvUsernameInfo.setText("Mini 1 Characters");
            RegisterActivity.tvUsernameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etUserName.setError(null);
            RegisterActivity.tvUsernameInfo.setVisibility(View.GONE);
        }

        if(firstName.isEmpty() || firstName.length() < 1 )
        {
//            RegisterActivity.etFirstName.setError("Minimum 3 Characters.");
            RegisterActivity.tvFirstnameInfo.setText("Mini 1 Characters");
            RegisterActivity.tvFirstnameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etFirstName.setError(null);
            RegisterActivity.tvFirstnameInfo.setVisibility(View.GONE);
        }

        if(lastName.isEmpty() || lastName.length() < 1 )
        {
//            RegisterActivity.etLastName.setError("Minimum 3 Characters.");
            RegisterActivity.tvLastnameInfo.setText("Mini 1 Characters");
            RegisterActivity.tvLastnameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etLastName.setError(null);
            RegisterActivity.tvLastnameInfo.setVisibility(View.GONE);
        }

        if (emailAddress.isEmpty())
        {
//            RegisterActivity.etEmail.setError("Not a valid Email Address");
            RegisterActivity.tvEmailInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            RegisterActivity.tvEmailInfo.setText("Not a valid Email Address");
            RegisterActivity.tvEmailInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etEmail.setError(null);
            RegisterActivity.tvEmailInfo.setVisibility(View.GONE);
        }

        if(contactNo.isEmpty()  )
        {
//            RegisterActivity.etPhone.setError("10 Characters Required");
            RegisterActivity.tvPhoneInfo.setText("Enter Contact Number");
            RegisterActivity.tvPhoneInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etPhone.setError(null);
            RegisterActivity.tvPhoneInfo.setVisibility(View.GONE);
        }

        if (password.isEmpty() || password.length() < 4)
        {
//            RegisterActivity.etPassword.setError("Minimum 4 Characters");
            RegisterActivity.tvPasswordInfo.setText("Min 4 Characters");
            RegisterActivity.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etPassword.setError(null);
            RegisterActivity.tvPasswordInfo.setVisibility(View.GONE);
        }

        if(rePassword.isEmpty())
        {
            RegisterActivity.tvRePasswordInfo.setText("Repeat password");
            RegisterActivity.tvRePasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (rePassword.equals(password))
        {
//            RegisterActivity.etConfirmPass.setError(null);
            RegisterActivity.tvRePasswordInfo.setVisibility(View.GONE);
        }
        else
        {
//            RegisterActivity.etConfirmPass.setError("Password does not match");
            RegisterActivity.tvRePasswordInfo.setText("Password does not match");
            RegisterActivity.tvRePasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }

    public static boolean updateRegisterValidate(String firstName, String lastName, String password, String rePassword, String contactNo)
    {
        boolean valid = true ;

        if(firstName.isEmpty() || firstName.length() < 1 )
        {
//            RegisterActivity.etFirstName.setError("Minimum 3 Characters.");
            MyAccountActivity.tvFirstNameInfo.setText("Mini 1 Characters");
            MyAccountActivity.tvFirstNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etFirstName.setError(null);
            MyAccountActivity.tvFirstNameInfo.setVisibility(View.GONE);
        }

        if(lastName.isEmpty() || lastName.length() < 1 )
        {
//            RegisterActivity.etLastName.setError("Minimum 3 Characters.");
            MyAccountActivity.tvLastNameInfo.setText("Mini 1 Characters");
            MyAccountActivity.tvLastNameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etLastName.setError(null);
            MyAccountActivity.tvLastNameInfo.setVisibility(View.GONE);
        }

        if(contactNo.isEmpty() )
        {
//            RegisterActivity.etPhone.setError("10 Characters Required");
            MyAccountActivity.tvPhoneInfo.setText("Enter Phone Number");
            MyAccountActivity.tvPhoneInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            RegisterActivity.etPhone.setError(null);
            MyAccountActivity.tvPhoneInfo.setVisibility(View.GONE);
        }

        if (password.isEmpty() || password.length() < 4)
        {
//            RegisterActivity.etPassword.setError("Minimum 4 Characters");
            MyAccountActivity.tvPasswordInfo.setText("Mini 4 Characters");
            MyAccountActivity.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else
        {
//            RegisterActivity.etPassword.setError(null);
            MyAccountActivity.tvPasswordInfo.setVisibility(View.GONE);
        }

        if(rePassword.isEmpty())
        {
            MyAccountActivity.tvAgainPasswordInfo.setText("Enter Re-Password");
            MyAccountActivity.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }
        else if (rePassword.equals(password))
        {
//            RegisterActivity.etConfirmPass.setError(null);
            MyAccountActivity.tvAgainPasswordInfo.setVisibility(View.GONE);
        }
        else
        {
//            RegisterActivity.etConfirmPass.setError("Password does not match");
            MyAccountActivity.tvAgainPasswordInfo.setText("Password does not match");
            MyAccountActivity.tvAgainPasswordInfo.setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }

    public static boolean validateLogin(String userName, String password)
    {
        boolean valid = true ;

        if(userName.isEmpty())
        {
//            LoginActivity.etLoginUser.setError(Html.fromHtml("<font color='red'>Username can't be empty</font>"));
           // LoginActivity.tvUsernameInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            LoginActivity.etLoginUser.setError(null);
            LoginActivity.tvUsernameInfo.setVisibility(View.GONE);
        }

        if(password.isEmpty())
        {
//            LoginActivity.etLoginPass.setError("Enter Password");
           // LoginActivity.tvPasswordInfo.setVisibility(View.VISIBLE);
            valid = false ;
        }
        else
        {
//            LoginActivity.etLoginPass.setError(null);
            LoginActivity.tvPasswordInfo.setVisibility(View.GONE);
        }

        return valid;
    }

}
