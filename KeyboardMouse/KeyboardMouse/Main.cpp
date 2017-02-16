#include <iostream>
#include <windows.h>
#include <thread>
#include <boost/thread.hpp>
#include <cstdio>
#include <ctime>

HHOOK keyboardHook;
HWND window;
clock_t start;
bool running = false;
bool hidden = true;
bool recentlyChanged = false;
bool F8Down = false;
bool UP = false;
bool DOWN = false;
bool LEFT = false;
bool RIGHT = false;
int longClickDuration = 400;

using namespace std;

void SetConsoleSpecs() {
	SetConsoleTitle("");

	MoveWindow(GetConsoleWindow(), 50, 50, 365, 300, true);

	COORD c;
	c.X = 40;
	c.Y = 18;
	SetConsoleScreenBufferSize(GetStdHandle(STD_OUTPUT_HANDLE), c);

	CONSOLE_CURSOR_INFO     cursorInfo;
	GetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE), &cursorInfo);
	cursorInfo.bVisible = false;
	SetConsoleCursorInfo(GetStdHandle(STD_OUTPUT_HANDLE), &cursorInfo);
}

void DisplayIntro() {
	cout << endl;
	cout << "             KEYBOARD MOUSE" << endl;
	cout << "            ----------------" << endl << endl << endl;
	cout << "          KEY            FUNCTION" << endl;
	cout << "   ----------------------------------" << endl;
	cout << "  |     Arrows     | Move the cursor |" << endl;
	cout << "   ----------------------------------" << endl;
	cout << "  |   Right CTRL   |   Left click    |" << endl;
	cout << "   ----------------------------------" << endl;
	cout << "  |   Right SHIFT  |   Right click   |" << endl;
	cout << "   ----------------------------------" << endl;
	cout << "  |       F8       |  Toggle on/off  |" << endl;
	cout << "   ----------------------------------" << endl;
	cout << "  |    Hold F8     |  Toggle display |" << endl;
	cout << "   ----------------------------------" << endl;
}


POINT CursorPosition() {
	POINT cursorPosition;
	GetCursorPos(&cursorPosition);
	return cursorPosition;
}

void MoveCursor(int direction) {
	int x = CursorPosition().x;
	int y = CursorPosition().y;
	switch (direction) {
	case 0:
		y--;
		break;
	case 1:
		y++;
		break;
	case 2:
		x--;
		break;
	case 3:
		x++;
		break;
	}
	SetCursorPos(x, y);
	Sleep(1);
}

void CursorControls() {
	while (true) {
		if (UP) MoveCursor(0);
		if (DOWN) MoveCursor(1);
		if (LEFT) MoveCursor(2);
		if (RIGHT) MoveCursor(3);
		if (F8Down &! recentlyChanged && ((clock() - start) >= longClickDuration)) {
			if (hidden) {
				ShowWindow(window, SW_SHOW);
				hidden = false;
			} else {
				ShowWindow(window, SW_HIDE);
				hidden = true;
			}
			recentlyChanged = true;
		}
	}
}
thread cursor(CursorControls);

void pauseProgram() {
	running = false;
	UP = false;
	DOWN = false;
	LEFT = false;
	RIGHT = false;
}

void unpauseProgram() {
	running = true;
}

LRESULT CALLBACK LowLevelKeyboardProc(int nCode, WPARAM wParam, LPARAM lParam) {
	if (nCode == HC_ACTION) {

		KBDLLHOOKSTRUCT key = *((KBDLLHOOKSTRUCT*)lParam);
		DWORD vk = key.vkCode;

		if (vk == VK_F8) {
			if ((wParam == WM_SYSKEYDOWN) || (wParam == WM_KEYDOWN)) {
				start = clock();
				F8Down = true;
			}
			if ((wParam == WM_SYSKEYUP) || (wParam == WM_KEYUP)) {
				F8Down = false;
				if (!recentlyChanged) {
					if (running) pauseProgram();
					else unpauseProgram();
				}
				recentlyChanged = false;
			}
			return 1;
		}

		if ((wParam == WM_SYSKEYDOWN) || (wParam == WM_KEYDOWN) && running) {
			{
				if (vk == VK_UP) {
					UP = true;
					return 1;
				}
				if (vk == VK_DOWN) {
					DOWN = true;
					return 1;
				}
				if (vk == VK_LEFT) {
					LEFT = true;
					return 1;
				}
				if (vk == VK_RIGHT) {
					RIGHT = true;
					return 1;
				}

				if (vk == VK_RCONTROL) {
					INPUT input = { 0 };
					input.type = INPUT_MOUSE;
					input.mi.dwFlags = MOUSEEVENTF_LEFTDOWN;
					::SendInput(1, &input, sizeof(INPUT));
					return 1;
				}

				if (vk == VK_RSHIFT)
				{
					INPUT input = { 0 };
					input.type = INPUT_MOUSE;
					input.mi.dwFlags = MOUSEEVENTF_RIGHTDOWN;
					::SendInput(1, &input, sizeof(INPUT));
					return 1;
				}
			}
		}

		if ((wParam == WM_SYSKEYUP) || (wParam == WM_KEYUP) && running) {
			{
				if (vk == VK_UP) UP = false;
				if (vk == VK_DOWN) DOWN = false;
				if (vk == VK_LEFT) LEFT = false;
				if (vk == VK_RIGHT) RIGHT = false;

				if (vk == VK_RCONTROL) {
					INPUT input = { 0 };
					input.type = INPUT_MOUSE;
					input.mi.dwFlags = MOUSEEVENTF_LEFTUP;
					::SendInput(1, &input, sizeof(INPUT));
					return 1;
				}

				if (vk == VK_RSHIFT)
				{
					INPUT input = { 0 };
					input.type = INPUT_MOUSE;
					input.mi.dwFlags = MOUSEEVENTF_RIGHTUP;
					::SendInput(1, &input, sizeof(INPUT));
					return 1;
				}
			}
		}
	}
	return CallNextHookEx(keyboardHook, nCode, wParam, lParam);
};

void ClickControls() {
	keyboardHook = SetWindowsHookEx(WH_KEYBOARD_LL, LowLevelKeyboardProc, 0, 0);
	MSG message;
	while (GetMessage(&message, NULL, 0, 0))
	{
		TranslateMessage(&message);
		DispatchMessage(&message);
	}
}



int main() {
	window = GetForegroundWindow();
	ShowWindow(window, SW_HIDE);
	SetConsoleSpecs();
	DisplayIntro();
	ClickControls();
	cursor.detach();
}