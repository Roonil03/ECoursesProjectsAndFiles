using System;
using System.Collections.Generic;

namespace EventEase.Services
{
    public class UserSessionService
    {
        public string CurrentUser { get; private set; } = "Guest User";
        public bool IsLoggedIn { get; private set; } = false;
        
        private Dictionary<int, int> _attendanceTracker = new();

        public event Action? OnStateChanged;

        public void SetUserSession(string username)
        {
            CurrentUser = username;
            IsLoggedIn = true;
            NotifyStateChanged();
        }

        public int GetAttendanceCount(int eventId)
        {
            return _attendanceTracker.TryGetValue(eventId, out int count) ? count : 0;
        }

        public void IncrementAttendance(int eventId)
        {
            if (_attendanceTracker.ContainsKey(eventId))
                _attendanceTracker[eventId]++;
            else
                _attendanceTracker[eventId] = 1;

            NotifyStateChanged();
        }

        private void NotifyStateChanged() => OnStateChanged?.Invoke();
    }
}
