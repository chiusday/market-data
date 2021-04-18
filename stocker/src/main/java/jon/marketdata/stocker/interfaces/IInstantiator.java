package jon.marketdata.stocker.interfaces;

@FunctionalInterface
public interface IInstantiator<R> {
    R newInstnce(Object... args);
}
