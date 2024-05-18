package bebop_upnp;

import org.jupnp.DefaultUpnpServiceConfiguration;
import org.jupnp.UpnpService;
import org.jupnp.UpnpServiceImpl;
import org.jupnp.binding.LocalServiceBindingException;
import org.jupnp.binding.annotations.AnnotationLocalServiceBinder;
import org.jupnp.model.DefaultServiceManager;
import org.jupnp.model.ValidationException;
import org.jupnp.model.meta.DeviceDetails;
import org.jupnp.model.meta.DeviceIdentity;
import org.jupnp.model.meta.LocalDevice;
import org.jupnp.model.meta.LocalService;
import org.jupnp.model.meta.ManufacturerDetails;
import org.jupnp.model.meta.ModelDetails;
import org.jupnp.model.types.DeviceType;
import org.jupnp.model.types.UDADeviceType;
import org.jupnp.model.types.UDN;

public class BinaryLight implements Runnable {

	public static void main(String[] args) {
		// Start a user thread that runs the UPnP stack
		Thread serverThread = new Thread(new BinaryLight());
		serverThread.setDaemon(false);
		serverThread.start();
	}

	@Override
	public void run() {
		try {

			final UpnpService upnpService = new UpnpServiceImpl(new DefaultUpnpServiceConfiguration());

			Runtime.getRuntime().addShutdownHook(new Thread(upnpService::shutdown));

			// Add the bound local device to the registry
			upnpService.startup();
			upnpService.getRegistry().addDevice(createDevice());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	LocalDevice createDevice() throws ValidationException, LocalServiceBindingException {

		DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier("Demo Binary Light"));

		DeviceType type = new UDADeviceType("BinaryLight", 1);

		DeviceDetails details = new DeviceDetails("Friendly Binary Light", new ManufacturerDetails("ACME"),
		                                          new ModelDetails("BinLight2000", "A demo light with on/off switch.", "v1"));

		LocalService<SwitchPower> switchPowerService = new AnnotationLocalServiceBinder().read(SwitchPower.class);

		switchPowerService.setManager(new DefaultServiceManager<>(switchPowerService, SwitchPower.class));

		return new LocalDevice(identity, type, details, switchPowerService);
	}

}
