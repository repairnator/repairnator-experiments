package org.nutz.el.opt.bit;

import org.nutz.el.opt.TwoTernary;
/**
 * 左移
 * @author juqkai(juqkai@gmail.com)
 *
 */
public class LeftShift extends TwoTernary{
    public int fetchPriority() {
        return 5;
    }
    public Object calculate() {
        Integer lval = (Integer) calculateItem(left);
        Integer rval = (Integer) calculateItem(right);
        return lval << rval;
    }
    public String fetchSelf() {
        return "<<";
    }

}
