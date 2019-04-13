package com.h0pkins3.familymap.models;

import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.h0pkins3.familymap.R;

import java.util.Map;

/** MapColor
 * The MapColor class stores a float color value for each different event type, and is used
 * for marker colors
 */
public class MapColor extends Color {

    private float color;

// ========================== Constructor ========================================
    public MapColor(String eventType)
    {
        color = Math.abs(eventType.hashCode() % 360);
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public float getColor()
    {
        return color;
    }
}
