package com.weirdduke.examples.routeguide;


import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class RouteGuideServerTest {

    private RouteGuideServer server;
    private ManagedChannel channel;
    private Collection<Feature> features;

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @After
    public void teardown() throws InterruptedException {
        server.stop();
    }

    @Before
    public void setup() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        features = new ArrayList<>();

        server = new RouteGuideServer(
                InProcessServerBuilder.forName(serverName).directExecutor(), 0, features);

        server.start();
        channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName).directExecutor().build());
    }

    @Test
    public void getFeature() {
        Point point = Point.newBuilder()
                .setLongitude(1)
                .setLatitude(1)
                .build();
        Feature unnamedFeature = Feature.newBuilder()
                .setName("")
                .setLocation(point)
                .build();
        RouteGuideGrpc.RouteGuideBlockingStub stub = RouteGuideGrpc.newBlockingStub(channel);

        Feature feature = stub.getFeature(point);
        assertEquals(unnamedFeature, feature);

        Feature namedFeature = Feature.newBuilder()
                .setName("name")
                .setLocation(point).build();
        features.add(namedFeature);
        feature = stub.getFeature(point);
        assertEquals(namedFeature, feature);
    }


}