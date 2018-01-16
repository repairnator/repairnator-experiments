package org.nutz.mvc.impl.processor;

import java.util.List;

import org.nutz.mvc.*;
import org.nutz.mvc.adaptor.PairAdaptor;

/**
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 * @author MingzFan(Mingz.Fan@gmail.com)
 */
public class AdaptorProcessor extends AbstractProcessor {

    private HttpAdaptor adaptor;

    @Override
    public void init(NutConfig config, ActionInfo ai) throws Throwable {
        adaptor = evalHttpAdaptor(config, ai);
    }

    public void process(ActionContext ac) throws Throwable {
        List<String> phArgs = ac.getPathArgs();
        Object[] args = adaptor.adapt(ac.getServletContext(),
                                      ac.getRequest(),
                                      ac.getResponse(),
                                      phArgs.toArray(new String[phArgs.size()]));
        ac.setMethodArgs(args);
        doNext(ac);
    }

    protected static HttpAdaptor evalHttpAdaptor(NutConfig config, ActionInfo ai) {
        HttpAdaptor re = evalObj(config, ai.getAdaptorInfo());
        if (null == re)
            re = new PairAdaptor();
        if (re instanceof HttpAdaptor2)
            ((HttpAdaptor2) re).init(ai);
        else
            re.init(ai.getMethod());
        return re;
    }
}
