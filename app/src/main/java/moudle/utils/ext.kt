package moudle.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.util.concurrent.Executors
import java.util.concurrent.Callable

@RequiresApi(Build.VERSION_CODES.N)
fun <T, R> Array<out T>.future(transform: (T) -> R): List<R> {
    val callable = this.map {
        Callable {
            transform.invoke(it)
        }
    }
    return Executors.newWorkStealingPool(callable.size).invokeAll(callable)
        .map { it.get() }
}

@RequiresApi(Build.VERSION_CODES.N)
fun <T, R> Iterable<out T>.future(transform: (T) -> R): List<R> {
    val callable = this.map {
        Callable {
            transform.invoke(it)
        }
    }
    return Executors.newWorkStealingPool(callable.size).invokeAll(callable)
        .map { it.get() }
}

