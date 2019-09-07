//package org.school.app;
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import java.awt.*;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@Component
//public class ServerStarting implements ApplicationListener<ContextRefreshedEvent> {
//	@Override
//	public void onApplicationEvent(ContextRefreshedEvent event) {
//		Desktop desktop = Desktop.getDesktop();
//		try {
//			desktop.browse(new URI("http://localhost:8080/catalog"));
//		} catch (IOException | URISyntaxException e) {
//			throw new RuntimeException(e);
//		}
//	}
//}
