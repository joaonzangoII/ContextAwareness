package app.androidgeofence.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by joaonzangoii on 11/22/16.
 */

public class Comment extends RealmObject {
    public static final String ID = "id";
    public static final String NAME = "name";
    @PrimaryKey
    private long id;
    private String title;
    private String body;
    private String user_id;
    private String safe_zone_id;
    private String created_at;
    private String updated_at;

    public static Comment createOrUpdate(final Realm realm,
                                         @NonNull final long id,
                                         @NonNull final String title,
                                         @NonNull final String body,
                                         @NonNull final String user_id,
                                         @NonNull final String safe_zone_id,
                                         @Nullable final String created_at,
                                         @Nullable final String updated_at) {
        final Comment comment = new Comment();
        comment.id = id;
        comment.title = title;
        comment.body = body;
        comment.user_id = user_id;
        comment.safe_zone_id = safe_zone_id;
        comment.body = body;
        comment.created_at = created_at;
        comment.updated_at = updated_at;
        return realm.copyToRealmOrUpdate(comment);
    }

    public String getSafezone(final Realm realm,
                              final long user_id) {
        return SafeZone.query(realm).byId(user_id).getName();
    }
}
