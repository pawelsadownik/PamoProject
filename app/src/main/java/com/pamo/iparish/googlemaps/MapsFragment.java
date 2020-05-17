package com.pamo.iparish.googlemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pamo.iparish.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback  {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    private static final List<Parish> PARISH_LIST = createList();

    private static List<Parish> createList() {
        List<Parish> result = new ArrayList<>();
        result.add(new Parish("Parafia Chrystusa Zbawiciela w Gdańsku", 54.4248351, 18.4641337));
        result.add(new Parish("Kościół pw. św. Jana Chrzciciela", 54.5441898, 18.4632969));
        result.add(new Parish("Bazylika Mariacka Wniebowzięcia Najświętszej Maryi Panny w Gdańsku", 54.3498409, 18.6532831));
        result.add(new Parish("Parafia św. Ignacego Loyoli w Gdańsku", 54.3339127, 18.6343494));
        return Collections.unmodifiableList(result);
    }

    private GoogleMap googleMap;
    private Marker lastSelectedMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                //googleMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
        List<LatLng> boundsList = new ArrayList<>();
        for (Parish parish : PARISH_LIST) {
            LatLng geoloc = new LatLng(parish.getLatitude(), parish.getLongitude());
            boundsList.add(geoloc);
            googleMap.addMarker(new MarkerOptions()
                    .position(geoloc)
                    .title(parish.getName()));
        }
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(boundsList.get(0))
                .include(boundsList.get(1))
                .include(boundsList.get(2))
                .include(boundsList.get(3))
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,100));
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Log.i("ParishMapPicker", "Picked " + marker.getTitle());
        // This causes the marker at Perth to bounce into position when it is clicked.
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        Toast.makeText(getActivity(), marker.getTitle() + " clicked",
                Toast.LENGTH_SHORT).show();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 2 * t);

                if (t > 0.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });

        lastSelectedMarker = marker;
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
