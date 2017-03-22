package eu.intermodalics.tango_ros_streamer;

import org.apache.commons.logging.Log;
import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.message.MessageFactory;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeConfiguration;

/**
 * Rosjava node that implements a service client.
 */
public class ServiceClientNode extends AbstractNodeMain {
  private static final String TAG = ServiceClientNode.class.getSimpleName();
  private Log mRosLog;
  private String mServiceName;
  private ServiceClient<AddTh, MyServiceResponse> mService;
  private MessageFactory mMessageFactory;

  public ServiceClientNode(String serviceName) {
    NodeConfiguration nodeConfiguration = NodeConfiguration.newPrivate();
    mMessageFactory = nodeConfiguration.getTopicMessageFactory();
    mServiceName = serviceName;
  }

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of(TAG);
  }

  public void onStart(ConnectedNode connectedNode) {
    mRosLog = connectedNode.getLog();
    try {
      mService = connectedNode.newServiceClient(mServiceName,
          SetMode._TYPE);
    } catch (ServiceNotFoundException e) {
      mRosLog.error("Service '" + mServiceName + "' not found!");
    }
  }

  public void callService() {
    MyServiceRequest MyServiceRequest = mService.newMessage();
    mService.call(MyServiceRequest, new ServiceResponseListener<MyServiceResponse>() {
      @Override
      public void onSuccess(MyServiceResponse response) {
        // Do something with the response message.
      }

      @Override
      public void onFailure(RemoteException e) {
        mRosLog.error("Error while calling service!", e);
      }
    });
  }
}
