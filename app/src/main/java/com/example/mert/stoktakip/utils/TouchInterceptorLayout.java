package com.example.mert.stoktakip.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * {@code TouchInterceptorLayout}, bir bileşeni içine alıp o bileşene erişimi engelliyor.
 * {@code TouchInterceptorLayout}'un içindeki bir bileşene dokunulmaya çalışılırsa içerdeki bileşen
 * yerine TouchInterceptorLayout'un onClickListener'ı devreye giriyor.
 * <p>
 * Örneğin {@code UrunAlFragment} ve {@code UrunSatFragment} sınıflarında {@code SearchView}
 * bileşenine dokunulunca ürün listesini gösteren bir dialog açılıyor, bu özellik
 * {@code TouchInterceptorLayout}'un {@code SearchView}'u altında tutmasıyla sağlanıyor.
 * <p>
 * Aynı zamanda {@code UrunGuncelleActivity}'de ürünün barkod numarası ve
 * {@code KullaniciGuncelleActivity}'de kullanıcının id'si güncellenemeyeceği için, bu alanları
 * içeren {@code EditText} bileşenleri {@code TouchInterceptorLayout} tarafından sarılıyor ve
 * dokunulmak istendiğinde {@code TouchInterceptorLayout}'ın {@code OnClickListener}'ı bu alanların
 * değiştirilemeyeceğini bildiren bir Toast mesajı gösteriyor.
 */

public class TouchInterceptorLayout extends FrameLayout {
    public TouchInterceptorLayout(Context context) {
        super(context);
    }

    public TouchInterceptorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchInterceptorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}