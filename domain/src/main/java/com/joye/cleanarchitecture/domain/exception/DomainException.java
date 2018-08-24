package com.joye.cleanarchitecture.domain.exception;

/**
 * 业务层异常
 * <p>
 * 一般的网络请求返回会定义成下面的格式
 * <p>
 * <pre><code>
 *  {
 *      respcode:0000,
 *      resperror:error msg,
 *      data: {
 *      }
 *  }
 *  </code></pre>
 * <p>
 * 对于业务层非正常的返回码需要统一处理
 * <p>
 * Created by joye on 2018/7/30.
 */

public class DomainException extends BaseException {

    public DomainException(int excCode, String excMsg) {
        super(excCode, excMsg);
    }

    public DomainException(Throwable throwable, int excCode, String excMsg) {
        super(throwable, excCode, excMsg);
    }
}
