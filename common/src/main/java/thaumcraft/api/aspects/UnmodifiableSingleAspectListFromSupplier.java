package thaumcraft.api.aspects;

import com.linearity.opentc4.simpleutils.SingleSupplierPairMap;

import java.util.Map;
import java.util.function.Supplier;

public class UnmodifiableSingleAspectListFromSupplier<Asp extends Aspect> extends UnmodifiableAspectList<Asp>{
    private final Map<Asp, Integer> view;
    public UnmodifiableSingleAspectListFromSupplier(Supplier<Asp> aspect,Supplier<Integer> amount) {
        this.view = new SingleSupplierPairMap<>(aspect,amount);
    }

    @Override
    public Map<Asp,Integer> getAspectView(){
        return view;
    }

}
