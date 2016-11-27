package app.androidgeofence.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.annotations.PrimaryKey;

public class SafeZone extends RealmObject {
    public static final String ID = "id";
    public static final String NAME = "name";
    @PrimaryKey
    public long id;
    public String name;
    public double latitude;
    public double longitude;
    public float radius;
    public String description;
    public String created_at;
    public String updated_at;

    public static SafeZone createOrUpdate(final Realm realm,
                                          @NonNull final long id,
                                          @NonNull final String name,
                                          @NonNull final LatLng latLng,
                                          @NonNull final float radius,
                                          @NonNull final String description,
                                          @NonNull final String created_at,
                                          @Nullable final String updated_at) {
        final SafeZone geo = new SafeZone();
        geo.id = id;
        geo.name = name;
        geo.latitude = latLng.latitude;
        geo.longitude = latLng.longitude;
        geo.radius = radius;
        geo.description = description;
        geo.created_at = created_at;
        geo.updated_at = updated_at;

        return realm.copyToRealmOrUpdate(geo);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public double getRadius() {
        return radius;
    }

    @NonNull
    @Override
    public String toString() {
        return id + " - " + name + " @ " + latitude + " and " + longitude;
    }

    // Static query variable
    @NonNull
    public static Query query(@NonNull final Realm realm) {
        return new Query(realm);
    }

    public static final class Query {
        private final RealmQuery<SafeZone> req;

        private Query(@NonNull final Realm realm) {
            req = realm.where(SafeZone.class);
        }

        @NonNull
        public RealmQuery<SafeZone> all() {
            return req;
        }

        @Nullable
        public SafeZone byId(final Long id) {
            return req.equalTo(ID, id).findFirst();
        }

        @Nullable
        public SafeZone byName(final String result) {
            return req.equalTo(NAME, result).findFirst();
        }
    }
}
