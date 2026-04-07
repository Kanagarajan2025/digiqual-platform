import { useState } from 'react'
import { ArrowLeft, BadgeCheck, FileSearch, Loader, ShieldCheck } from 'lucide-react'
import { Link } from 'react-router-dom'
import { verifyCertificate } from '../api/studentApi'

export default function CertificateVerificationPage() {
  const [studentId, setStudentId] = useState('')
  const [surname, setSurname] = useState('')
  const [dob, setDob] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [result, setResult] = useState(null)
  const gatewayBaseUrl = import.meta.env.VITE_API_GATEWAY_BASE_URL || 'http://localhost:8090/api'

  const handleVerify = async (event) => {
    event.preventDefault()
    setError('')
    setResult(null)

    if (!studentId.trim() || (!surname.trim() && !dob.trim())) {
      setError('Please enter Student ID and either surname or date of birth.')
      return
    }

    setLoading(true)
    try {
      const response = await verifyCertificate(studentId.trim(), surname.trim(), dob.trim())
      if (response.valid) {
        setResult(response)
      } else {
        setError(response.message || 'Verification failed.')
      }
    } catch (err) {
      const backendMessage = err?.response?.data?.message
      setError(backendMessage || 'Unable to verify certificate right now.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen px-4 py-10 md:px-6">
      <div className="mx-auto max-w-4xl">
        <div className="mb-6 flex items-center justify-between">
          <Link to="/" className="inline-flex items-center gap-2 rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm text-slate-700 hover:border-orange-400 hover:text-orange-700">
            <ArrowLeft className="h-4 w-4" />
            Back to Login
          </Link>
          <div className="inline-flex items-center gap-2 rounded-full border border-emerald-100 bg-emerald-50 px-3 py-1 text-xs font-semibold uppercase tracking-[0.14em] text-emerald-700">
            <ShieldCheck className="h-3.5 w-3.5" />
            Public Verification
          </div>
        </div>

        <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-[0_20px_70px_rgba(15,23,42,0.1)] md:p-8">
          <h1 className="font-display text-3xl font-semibold text-slate-950">Certificate Verification</h1>
          <p className="mt-2 text-sm text-slate-600">
            Enter the student ID and surname to verify an issued DIGIQUAL certificate.
          </p>

          <form onSubmit={handleVerify} className="mt-6 grid gap-4 md:grid-cols-3 md:items-end">
            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-700">Student ID</label>
              <input
                type="text"
                value={studentId}
                onChange={(e) => setStudentId(e.target.value.toUpperCase())}
                placeholder="DGQ-2026-001"
                className="w-full rounded-xl border border-slate-300 px-3 py-2.5 text-sm outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-100"
              />
            </div>

            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-700">Surname</label>
              <input
                type="text"
                value={surname}
                onChange={(e) => setSurname(e.target.value)}
                placeholder="Bennett"
                className="w-full rounded-xl border border-slate-300 px-3 py-2.5 text-sm outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-100"
              />
            </div>

            <div>
              <label className="mb-1.5 block text-sm font-medium text-slate-700">Date of Birth (optional)</label>
              <input
                type="date"
                value={dob}
                onChange={(e) => setDob(e.target.value)}
                className="w-full rounded-xl border border-slate-300 px-3 py-2.5 text-sm outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-100"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="inline-flex h-[42px] items-center justify-center gap-2 rounded-xl bg-gradient-to-r from-orange-500 to-orange-600 px-4 text-sm font-semibold text-white hover:from-orange-600 hover:to-orange-700 disabled:cursor-not-allowed disabled:opacity-60 md:col-span-3"
            >
              {loading ? <Loader className="h-4 w-4 animate-spin" /> : <FileSearch className="h-4 w-4" />}
              Verify
            </button>
          </form>

          {error && (
            <div className="mt-4 rounded-xl border border-rose-200 bg-rose-50 px-4 py-2 text-sm text-rose-700">
              {error}
            </div>
          )}

          {result && (
            <div className="mt-6 rounded-2xl border border-emerald-200 bg-emerald-50 p-5">
              <div className="mb-3 inline-flex items-center gap-2 rounded-full bg-white px-3 py-1 text-xs font-semibold uppercase tracking-[0.14em] text-emerald-700">
                <BadgeCheck className="h-4 w-4" />
                Verified
              </div>

              <div className="grid gap-3 text-sm text-slate-700 md:grid-cols-2">
                <p><span className="font-semibold">Student ID:</span> {result.studentId}</p>
                <p><span className="font-semibold">Student Name:</span> {result.studentName}</p>
                <p><span className="font-semibold">Course:</span> {result.courseName}</p>
                <p><span className="font-semibold">Certificate ID:</span> {result.certificateId || 'Pending'}</p>
                <p><span className="font-semibold">Status:</span> {result.certificateStatus}</p>
                <p><span className="font-semibold">DOB:</span> {result.dateOfBirth || 'Not available'}</p>
                <p><span className="font-semibold">Issued:</span> {result.issuedAt ? new Date(result.issuedAt).toLocaleDateString() : 'Not issued yet'}</p>
              </div>

              {result.downloadPath && (
                <a
                  href={`${gatewayBaseUrl.replace(/\/$/, '')}${result.downloadPath}`}
                  className="mt-4 inline-flex rounded-xl bg-emerald-600 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-700"
                >
                  Download Certificate PDF
                </a>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
