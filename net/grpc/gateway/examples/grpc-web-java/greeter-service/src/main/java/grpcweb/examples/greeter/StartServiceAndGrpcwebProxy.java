package grpcweb.examples.greeter;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpcweb.GrpcPortNumRelay;
import io.grpcweb.JettyWebserverForGrpcwebTraffic;

/**
 * This class starts the service on a port (property: grpc-port)
 * and starts the grpc-web proxy on a different port (property: grpc-web-port).
 */
class StartServiceAndGrpcwebProxy {
  private void startGrpcService(int port) throws Exception {
    Server grpcServer = ServerBuilder.forPort(port)
        .addService((BindableService) new GreeterService())
        .build();
    grpcServer.start();
    System.out.println("****  started gRPC Service on port# " + port);
  }

  void start() throws Exception {
    int grpcPort = Util.getGrpcServicePortNum();
    int grpcWebPort = Util.getGrpcwebServicePortNum();

    // Start the Grpc service on grpc-port
    startGrpcService(grpcPort);

    // Start the grpc-web proxy on grpc-web-port.
    (new JettyWebserverForGrpcwebTraffic(grpcWebPort)).start();

    // grpc-web proxy needs to know the grpc-port# so it could connect to the grpc service.
    GrpcPortNumRelay.setGrpcPortNum(grpcPort);
  }
}
