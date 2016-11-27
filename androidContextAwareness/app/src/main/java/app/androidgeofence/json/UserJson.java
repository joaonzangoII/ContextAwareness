package app.androidgeofence.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.annotations.PrimaryKey;

public class UserJson {
    public static final String ID = "id";
    public static final String NAME = "firstname";
    @PrimaryKey
    public long id;
    public String firstname;
    public String middlename;
    public String lastname;
    public String id_number;
    public String email;
    public String gender;
    public String date_of_birth;
    public String user_type;
    public String picture_url;
    public String password;
    public String created_at;
    public String updated_at;

    public UserJson(@NonNull final long id,
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
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.id_number = id_number;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.user_type = user_type;
        this.picture_url = picture_url;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
