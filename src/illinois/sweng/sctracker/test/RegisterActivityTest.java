package illinois.sweng.sctracker.test;

import illinois.sweng.sctracker.R;
import illinois.sweng.sctracker.RegisterActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivityTest extends ActivityInstrumentationTestCase2<RegisterActivity> {
	private RegisterActivity mActivity;
	private EditText mEmailField, mPasswordField, mPasswordConfirmField;
	private Button mCreateAccountButton;
	
	public RegisterActivityTest() {
		super("illinois.sweng.sctracker", RegisterActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
		
		mEmailField = (EditText) mActivity.findViewById(illinois.sweng.sctracker.R.id.emailEditText);
		mPasswordField = (EditText) mActivity.findViewById(illinois.sweng.sctracker.R.id.passwordTextEdit);
		mPasswordConfirmField = (EditText) mActivity.findViewById(illinois.sweng.sctracker.R.id.passwordConfirmTextEdit);
		mCreateAccountButton = (Button)  mActivity.findViewById(illinois.sweng.sctracker.R.id.registerCreateButton);
	}
	
	/**
	 * Test that the activity initialized correctly
	 */
	public void testPreConditions() {
		assertTrue(mEmailField.getText().toString().equals(""));
		assertTrue(mPasswordField.getText().toString().equals(""));
		assertTrue(mPasswordConfirmField.getText().toString().equals(""));
	}
	
	/**
	 * Tests that when an invalid email address is entered, no account is created
	 * and the email field is cleared.
	 */
	public void testInvalidEmailAddress() {
		String prefsFile = mActivity.getResources().getString(R.string.preferencesFilename);
		String key = mActivity.getResources().getString(R.string.preferencesUserpass);
		
		SharedPreferences sharedPreferences = mActivity.getSharedPreferences(prefsFile, RegisterActivity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); 
		editor.putString(key, ":");
		editor.commit();
		
		mActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mEmailField.setText("invalid email");
						mCreateAccountButton.requestFocus();
					}
				}
		);
		
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		
		assertEquals(mEmailField.getText().toString(), "");
		assertFalse(mCreateAccountButton.getText().toString().
				equals(illinois.sweng.sctracker.R.string.registerNewAccountSuccess));
	}
	
	/**
	 * Tests that when non-matching passwords are entered, no account is created
	 * and the password fields are both cleared. The entered valid email address
	 * is maintained.
	 */
	public void testInvalidPasswords() {
		String prefsFile = mActivity.getResources().getString(R.string.preferencesFilename);
		String key = mActivity.getResources().getString(R.string.preferencesUserpass);
		
		SharedPreferences sharedPreferences = mActivity.getSharedPreferences(prefsFile, RegisterActivity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit(); 
		editor.putString(key, ":");
		editor.commit();
		
		final String validEmail = "valid@email.com";
		mActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mEmailField.setText(validEmail);
						mPasswordField.setText("password1");
						mPasswordConfirmField.setText("password2");
						mCreateAccountButton.requestFocus();
					}
				}
		);
		
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		
		assertEquals(mEmailField.getText().toString(), validEmail);
		assertEquals(mPasswordField.getText().toString(), "");
		assertEquals(mPasswordConfirmField.getText().toString(), "");
		assertFalse(mCreateAccountButton.getText().toString().
				equals(illinois.sweng.sctracker.R.string.registerNewAccountSuccess));
	}
}
