package site.duqian.test.utils
/**
 * description:命令执行结果
 * @author 杜乾 Created on 2018/7/12 - 14:10.
 * E-mail:duqian2010@gmail.com
 */
class CommandResult {
    //CommandResult{result=0, successMsg='Android Debug Bridge version 1.0.31', errorMsg=''}
    var result = -1
    var successMsg: String = ""
    var errorMsg: String = ""

    constructor(result: Int) {
        this.result = result
    }

    constructor(result: Int, successMsg: String, errorMsg: String) {
        this.result = result
        this.successMsg = successMsg
        this.errorMsg = errorMsg
    }

    override fun toString(): String {
        return "CommandResult{" +
                "result=" + result +
                ", successMsg='" + successMsg + '\''.toString() +
                ", errorMsg='" + errorMsg + '\''.toString() +
                '}'.toString()
    }
}