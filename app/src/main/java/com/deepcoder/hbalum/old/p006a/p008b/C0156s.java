package com.deepcoder.hbalum.old.p006a.p008b;

import java.util.ArrayList;
import java.util.Iterator;

public class C0156s {
    public C0153o c0153o;
    ArrayList f827b = new ArrayList();
    public String positionCode;

    public C0156s m1325a(String str) {
        if (positionCode.equals(str)) {
            return this;
        }
        if (f827b != null && f827b.size() > 0) {
            for (int i = 0; i < f827b.size(); i++) {
                C0156s c0156s = ((C0156s) f827b.get(i)).m1325a(str);
                if (c0156s != null) {
                    return c0156s;
                }
            }
        }
        return null;
    }

    public ArrayList<C0156s> m1326a() {
        ArrayList<C0156s> arrayList = new ArrayList<>();
        if (this.f827b == null || this.f827b.size() == 0) {
            arrayList.add(this);
        } else {
            Iterator it = this.f827b.iterator();
            while (it.hasNext()) {
                arrayList.addAll(((C0156s) it.next()).m1326a());
            }
        }
        return arrayList;
    }
}
