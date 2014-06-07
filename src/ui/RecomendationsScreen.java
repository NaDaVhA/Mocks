package ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import recommender.RecommenderEngineAdapter;
import utilities.Pair;
import core.ApplicationInterface;

public class RecomendationsScreen extends Screen {

	private Button homeBtn = null;
	private List<String> recommendedFriends = null;
	private List<Pair<String, String>> recommendedSongs = null;
	private String loggedInUserName;

	public RecomendationsScreen(Display display, Shell shell, ApplicationInterface engine) {
		super(display, shell, engine);
		System.out.println("RecomendationsScreen-constructor()");
	}

	@Override
	public void createScreen() {

		System.out.println("RecomendationsScreen-createScreen()");

		// Get Logged In Username
		loggedInUserName = engine.getUsername();

		// Initialize UI
		initializeControls();

		// Get Recommendations From RecommenderEngine
		runRecommendations();
	}

	private void runRecommendations() {

		// Get Friends Recommendations
		try {
			recommendedFriends = RecommenderEngineAdapter.getInstance().recommendFriends(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedFriends = new ArrayList<>();
		}

		// Get Songs Recommendations
		try {
			recommendedSongs = RecommenderEngineAdapter.getInstance().recommendSongs(loggedInUserName, 10);
		} catch (Exception e) {
			recommendedSongs = new ArrayList<>();
		}
	}

	private void initializeControls() {

		// Home Button
		homeBtn = new Button(getShell(), SWT.NONE);
		homeBtn.setText("Home");
		FormData homeBtnFormData = new FormData();
		homeBtnFormData.width = 110;
		homeBtnFormData.height = 30;
		homeBtnFormData.right = new FormAttachment(20, 0);
		homeBtnFormData.bottom = new FormAttachment(100, 0);
		homeBtn.setLayoutData(homeBtnFormData);

		homeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeScreen();
				mainScreen mainScreen = new mainScreen(getDisplay(),
						getShell(), engine, engine.getUsername());
				mainScreen.createScreen();
			}
		});

		this.getShell().layout();
	}

	@Override
	protected void disposeScreen() {
		System.out.println("RecomendationsScreen-disposeScreen()");
		homeBtn.dispose();
	}

	@Override
	protected void hideScreen() {
		System.out.println("RecomendationsScreen-hideScreen()");
	}

	@Override
	protected void showScreen() {
		System.out.println("RecomendationsScreen-showScreen()");
	}

}
