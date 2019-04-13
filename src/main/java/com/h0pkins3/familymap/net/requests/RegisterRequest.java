/* For my brackets, I use a different style with methods than I do with any of the other brackets.
I talked to Professor Rodham and he approved this as long as I was consistent */


package com.h0pkins3.familymap.net.requests;

/** RegisterRequest is the request that is sent from the client, contains:
 * Username
 * password
 * email
 * first and last name
 * a PersonID
 */
public class RegisterRequest {

        private String userName;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String gender;

        // ========================== Constructors ========================================

        public RegisterRequest()
        {
            userName = null;
            password = null;
            email = null;
            firstName = null;
            lastName = null;
            gender = null;
        }

        public RegisterRequest(String userNameID, String userPassword, String userEmail,
                               String userFirstName, String userLastName, String userGender)
        {
            this.userName = userNameID;
            this.password = userPassword;
            this.email = userEmail;
            this.firstName = userFirstName;
            this.lastName = userLastName;
            this.gender = userGender;
        }

        //_______________________________ Getters and Setters __________________________________________

        public String getUserNameID()
        {
            return userName;
        }

        public void setUserNameID(String userNameID)
        {
            this.userName = userNameID;
        }

        public String getUserPassword()
        {
            return password;
        }

        public void setUserPassword(String userPassword)
        {
            this.password = userPassword;
        }

        public String getUserEmail()
        {
            return email;
        }

        public void setUserEmail(String userEmail)
        {
            this.email = userEmail;
        }

        public String getUserFirstName()
        {
            return firstName;
        }

        public void setUserFirstName(String userFirstName)
        {
            this.firstName = userFirstName;
        }

        public String getUserLastName()
        {
            return lastName;
        }

        public void setUserLastName(String userLastName)
        {
            this.lastName = userLastName;
        }

        public String getUserGender()
        {
            return gender;
        }

        public void setUserGender(String userGender)
        {
            this.gender = userGender;
        }

}
