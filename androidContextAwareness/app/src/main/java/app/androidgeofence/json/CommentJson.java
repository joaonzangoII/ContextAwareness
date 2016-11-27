package app.androidgeofence.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import app.androidgeofence.model.SafeZone;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by joaonzangoii on 11/22/16.
 */

public class CommentJson {
    public static final String ID = "id";
    public static final String NAME = "name";
    @PrimaryKey
    public long id;
    public String title;
    public String body;
    public String user_id;
    public String safe_zone_id;
    public String created_at;
    public String updated_at;

    public CommentJson(@NonNull final long id,
                       @NonNull final String title,
                       @NonNull final String body,
                       @NonNull final String user_id,
                       @NonNull final String safe_zone_id,
                       @Nullable final String created_at,
                       @Nullable final String updated_at) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.user_id = user_id;
        this.safe_zone_id = safe_zone_id;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getSafeZone(final Realm realm,
                              final String safe_zone_id) {
        if(SafeZone.query(realm)
                .byId(Long.valueOf(safe_zone_id))==null){
            return "";
        }
        return SafeZone.query(realm)
                .byId(Long.valueOf(safe_zone_id)).getName();
    }
}
