package app.androidgeofence.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.androidgeofence.SessionManager;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    public static final String ID = "id";
    public static final String FIRSTNAME = "firstname";
    @PrimaryKey
    private long id;
    private String firstname;
    private String middlename;
    private String lastname;
    private String id_number;
    private String email;
    private String gender;
    private String date_of_birth;
    private String user_type;
    private String picture_url;
    private String password;
    private String created_at;
    private String updated_at;

    public static User createOrUpdate(final Realm realm,
                                      @NonNull final long id,
                                      @NonNull final String firstname,
                                      @NonNull final String middlename,
                                      @NonNull final String lastname,
                                      @NonNull final String id_number,
                                      @NonNull final String email,
                                      @NonNull final String gender,
                                      @NonNull final String date_of_birth,
                                      @NonNull final String user_type,
                                      @NonNull final String picture_url,
                                      @NonNull final String password,
                                      @Nullable final String created_at,
                                      @Nullable final String updated_at) {
        final User user = new User();
        user.id = id;
        user.firstname = firstname;
        user.middlename = middlename;
        user.lastname = lastname;
        user.id_number = id_number;
        user.email = email;
        user.gender = gender;
        user.date_of_birth = date_of_birth;
        user.user_type = user_type;
        user.picture_url = picture_url;
        user.password = password;
        user.created_at = created_at;
        user.updated_at = updated_at;
        return realm.copyToRealmOrUpdate(user);
    }

    //    @Override
    //    public boolean equals(Object obj) {
    //        return super.equals(obj);
    //    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }


    public String getMiddlename() {
        return "";
        //return middlename.equals("") || middlename == null ?  " " +  middlename : "";
    }

    public String getPicture_url(final SessionManager session) {
        return session.getServerUrl() + picture_url;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullname() {
        return firstname + getMiddlename() + " " + lastname;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public String getIdNumber() {
        return id_number;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender.substring(0, 1).toUpperCase() + gender.substring(1);
    }

    public String getUserType() {
        return user_type;
    }

    public String getPassword() {
        return password;
    }

    // Static query variable
    @NonNull
    public static Query query(@NonNull final Realm realm) {
        return new Query(realm);
    }

    public static final class Query {
        private final RealmQuery<User> req;

        private Query(@NonNull final Realm realm) {
            req = realm.where(User.class);
        }

        @NonNull
        public RealmQuery<User> all() {
            return req;
        }

        @Nullable
        public User byId(final Long id) {
            return req.equalTo(ID, id).findFirst();
        }

        @Nullable
        public User byName(final String result) {
            return req.equalTo(FIRSTNAME, result).findFirst();
        }
    }
}
