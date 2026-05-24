package thaumcraft.api.wands;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

//same to super but capacity wont change
//if all component provide capacity wont change it will surly fast capacity calculation(we'll cache capacity in stack)
public interface IImmutableAspectCapacityOwnerComponent<Asp extends Aspect> extends IAspectCapacityOwnerComponent<Asp>
{
    @Unmodifiable
    @Contract(pure = true)
    CentiVisList<Asp> getCentiVisCapacity();
}
