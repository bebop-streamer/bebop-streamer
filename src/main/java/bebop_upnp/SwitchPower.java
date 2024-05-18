package bebop_upnp;

import org.jupnp.binding.annotations.UpnpAction;
import org.jupnp.binding.annotations.UpnpInputArgument;
import org.jupnp.binding.annotations.UpnpOutputArgument;
import org.jupnp.binding.annotations.UpnpService;
import org.jupnp.binding.annotations.UpnpServiceId;
import org.jupnp.binding.annotations.UpnpServiceType;
import org.jupnp.binding.annotations.UpnpStateVariable;
import org.jupnp.model.action.ActionException;
import org.jupnp.model.types.ErrorCode;

@UpnpService(
		serviceId = @UpnpServiceId("SwitchPower"),
		serviceType = @UpnpServiceType(value = "SwitchPower", version = 1)
)
public class SwitchPower {

	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private boolean target = false;

	@UpnpStateVariable(defaultValue = "0")
	private boolean status = false;

	@UpnpAction
	public void setTarget(@UpnpInputArgument(name = "NewTargetValue")
	                      boolean newTargetValue) {
		target = newTargetValue;
		status = newTargetValue;
		System.out.println("Switch is: " + status);
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "RetTargetValue"))
	public boolean getTarget() {
		return target;
	}

	@UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
	public boolean getStatus() throws ActionException {
		// If you want to pass extra UPnP information on error:
		throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
	}

}
