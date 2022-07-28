package com.lxn.utilone.anr

import android.os.Looper
import java.io.Serializable
import java.util.*

/**
 * @author：李晓楠
 * 时间：2022/7/27 17:22
 */
public class ANRError : Error {
    class `$` constructor(private val _name: String, private val _stackTrace: Array<StackTraceElement>) : Serializable {
        inner class _Thread constructor(other: _Thread?) : Throwable(_name, other) {
            override fun fillInStackTrace(): Throwable {
                stackTrace = _stackTrace
                return this
            }
        }
    }

    private val serialVersionUID = 1L

    public var duration:Long = 0

    private constructor(st: `$`._Thread, duration: Long):super("Application Not Responding for at least $duration ms.", st){
        this.duration = duration
    }

    override fun fillInStackTrace(): Throwable {
        stackTrace = arrayOf()
        return this
    }

    companion object{
        @JvmStatic
        fun New( duration: Long,prefix:String,logThreadsWithoutStackTrace:Boolean = false):ANRError{
            val mainThread = Looper.getMainLooper().thread
            val stackTraces:TreeMap<Thread,Array<StackTraceElement>> = TreeMap(Comparator<Thread> { lhs, rhs ->
                if( lhs == rhs) return@Comparator 0
                if(lhs == mainThread) return@Comparator 1
                if(rhs == mainThread) return@Comparator -1
                rhs?.name?.compareTo(lhs!!.name) ?: 0
            })
            for (entry in Thread.getAllStackTraces().entries){
                if(entry.key == mainThread || entry.key.name.startsWith(prefix)
                    && (logThreadsWithoutStackTrace || entry.value.isNotEmpty())){
                    stackTraces[entry.key] = entry.value

                }
            }
            if(!stackTraces.containsKey(mainThread)){
                stackTraces[mainThread] = mainThread.stackTrace
            }
            var tst: `$`._Thread? = null
            for (entry in stackTraces){
                tst = `$`(getThreadTitle(entry.key),entry.value)._Thread(tst)
            }
            return ANRError(tst!!,duration)
        }
        @JvmStatic
        fun NewMainOnly(duration:Long):ANRError{
            val mainThread = Looper.getMainLooper().thread
            val mainStackTrace = mainThread.stackTrace
            return ANRError(`$`(getThreadTitle(mainThread), mainStackTrace)._Thread(null), duration)
        }

        private fun getThreadTitle(thread: Thread): String {
            return thread.name + " (state = " + thread.state + ")"
        }
    }
}