import { useEffect, useMemo, useState } from 'react'
import AppShell from '../components/AppShell'
import heroAdmin from '../assets/hero-admin.svg'
import {
  approveBatch,
  approvePartner,
  approveStudent,
  getAdminDashboard,
  rejectStudent,
  returnBatch,
  suspendPartner,
  updateCertificateStage,
} from '../api/adminApi'

const certificateStages = ['QA review', 'Signature pending', 'Ready to issue', 'Issued']

export default function AdminDashboard() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [actionMessage, setActionMessage] = useState('')
  const [workingKey, setWorkingKey] = useState('')

  const loadDashboard = async (showLoading = false) => {
    if (showLoading) {
      setLoading(true)
    }

    try {
      const response = await getAdminDashboard()
      setData(response)
      setError('')
    } catch (err) {
      setError(err.response?.data?.message || 'Unable to load admin dashboard data.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadDashboard(true)
  }, [])

  const runAction = async (key, action, successMessage) => {
    setWorkingKey(key)
    setActionMessage('')
    setError('')

    try {
      await action()
      setActionMessage(successMessage)
      await loadDashboard()
    } catch (err) {
      setError(err.response?.data?.message || 'Action failed. Please try again.')
    } finally {
      setWorkingKey('')
    }
  }

  const confirmAndRun = (message, key, action, successMessage) => {
    if (!window.confirm(message)) {
      return
    }

    runAction(key, action, successMessage)
  }

  const promptAndReturnBatch = (batch) => {
    const reason = window.prompt(`Return ${batch.batchCode} to ${batch.institute}. Enter the reason for changes:`)

    if (reason === null) {
      return
    }

    const trimmedReason = reason.trim()
    if (!trimmedReason) {
      setError('A return reason is required.')
      return
    }

    runAction(
      `return-batch-${batch.id}`,
      () => returnBatch(batch.id, trimmedReason),
      `${batch.batchCode} returned to partner.`
    )
  }

  const stats = useMemo(() => {
    if (data?.stats?.length) {
      return data.stats
    }

    return []
  }, [data, loading])

  const pendingPartners = data?.pendingPartners || []
  const pendingStudents = data?.pendingStudents || []
  const submittedBatches = data?.submittedBatches || []
  const capacityControls = data?.capacityControls || []
  const certificateQueue = data?.certificateQueue || []
  const quickActions = data?.quickActions || []
  const highlights = data?.highlights || []

  return (
    <AppShell
      role="ADMIN"
      title="Super Admin Command Center"
      subtitle="Approve partner institutes, manage student records, set batch controls, and oversee certificate issuance from one place."
      accentClass="from-[#0f766e] via-[#0f766e] to-[#ea580c]"
      heroImage={heroAdmin}
      heroBadge="Compliance View"
      stats={stats}
      quickLinks={quickActions}
      highlights={highlights}
    >
      {error ? <div className="rounded-2xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm text-rose-700">{error}</div> : null}
      {actionMessage ? <div className="rounded-2xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">{actionMessage}</div> : null}

      <div className="grid gap-6 xl:grid-cols-[1.1fr_0.9fr]">
        <div className="space-y-6">
          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <div className="mb-4 flex items-center justify-between gap-3">
              <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Partner onboarding queue</h2>
              <span className="rounded-full bg-amber-50 px-3 py-1 text-xs font-semibold text-amber-700">{pendingPartners.length} pending</span>
            </div>
            <div className="overflow-hidden rounded-2xl border border-slate-200">
              <table className="min-w-full text-left text-sm">
                <thead className="bg-slate-100 text-slate-600">
                  <tr>
                    <th className="px-4 py-3 font-medium">Institute</th>
                    <th className="px-4 py-3 font-medium">Country</th>
                    <th className="px-4 py-3 font-medium">Submitted</th>
                    <th className="px-4 py-3 font-medium">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {pendingPartners.map((partner) => (
                    <tr key={partner.id ?? partner.institute} className="border-t border-slate-100 bg-white">
                      <td className="px-4 py-3 text-slate-900">{partner.institute}</td>
                      <td className="px-4 py-3 text-slate-700">{partner.country}</td>
                      <td className="px-4 py-3 text-slate-700">{partner.submitted}</td>
                      <td className="px-4 py-3">
                        <div className="flex gap-2">
                          <button
                            type="button"
                            disabled={workingKey === `approve-partner-${partner.id}`}
                            onClick={() => runAction(`approve-partner-${partner.id}`, () => approvePartner(partner.id), `${partner.institute} approved.`)}
                            className="rounded-lg bg-emerald-600 px-3 py-1.5 text-xs font-semibold text-white disabled:opacity-60"
                          >
                            Approve
                          </button>
                          <button
                            type="button"
                            disabled={workingKey === `suspend-partner-${partner.id}`}
                            onClick={() => runAction(`suspend-partner-${partner.id}`, () => suspendPartner(partner.id), `${partner.institute} suspended.`)}
                            className="rounded-lg border border-slate-300 px-3 py-1.5 text-xs font-semibold text-slate-700 disabled:opacity-60"
                          >
                            Suspend
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {pendingPartners.length === 0 && !loading ? (
                    <tr className="border-t border-slate-100 bg-white">
                      <td colSpan={4} className="px-4 py-4 text-center text-slate-500">No pending partners</td>
                    </tr>
                  ) : null}
                </tbody>
              </table>
            </div>
          </div>

          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <div className="mb-4 flex items-center justify-between gap-3">
              <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Student approval queue</h2>
              <span className="rounded-full bg-sky-50 px-3 py-1 text-xs font-semibold text-sky-700">{pendingStudents.length} in review</span>
            </div>
            <div className="overflow-hidden rounded-2xl border border-slate-200">
              <table className="min-w-full text-left text-sm">
                <thead className="bg-slate-100 text-slate-600">
                  <tr>
                    <th className="px-4 py-3 font-medium">Learner</th>
                    <th className="px-4 py-3 font-medium">Course</th>
                    <th className="px-4 py-3 font-medium">Partner</th>
                    <th className="px-4 py-3 font-medium">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {pendingStudents.map((student) => (
                    <tr key={student.id} className="border-t border-slate-100 bg-white">
                      <td className="px-4 py-3 text-slate-900">{student.name}</td>
                      <td className="px-4 py-3 text-slate-700">{student.course}</td>
                      <td className="px-4 py-3 text-slate-700">{student.partner}</td>
                      <td className="px-4 py-3">
                        <div className="flex gap-2">
                          <button
                            type="button"
                            disabled={workingKey === `approve-student-${student.id}`}
                            onClick={() => runAction(`approve-student-${student.id}`, () => approveStudent(student.id), `${student.name} approved.`)}
                            className="rounded-lg bg-slate-900 px-3 py-1.5 text-xs font-semibold text-white disabled:opacity-60"
                          >
                            Approve
                          </button>
                          <button
                            type="button"
                            disabled={workingKey === `reject-student-${student.id}`}
                            onClick={() => runAction(`reject-student-${student.id}`, () => rejectStudent(student.id), `${student.name} rejected.`)}
                            className="rounded-lg border border-rose-200 px-3 py-1.5 text-xs font-semibold text-rose-700 disabled:opacity-60"
                          >
                            Reject
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {pendingStudents.length === 0 && !loading ? (
                    <tr className="border-t border-slate-100 bg-white">
                      <td colSpan={4} className="px-4 py-4 text-center text-slate-500">No learners awaiting review</td>
                    </tr>
                  ) : null}
                </tbody>
              </table>
            </div>
          </div>

          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <div className="mb-4 flex items-center justify-between gap-3">
              <h2 className="font-display text-2xl font-semibold tracking-tight text-slate-950">Submitted batch review</h2>
              <span className="rounded-full bg-violet-50 px-3 py-1 text-xs font-semibold text-violet-700">{submittedBatches.length} awaiting decision</span>
            </div>
            <div className="overflow-hidden rounded-2xl border border-slate-200">
              <table className="min-w-full text-left text-sm">
                <thead className="bg-slate-100 text-slate-600">
                  <tr>
                    <th className="px-4 py-3 font-medium">Batch</th>
                    <th className="px-4 py-3 font-medium">Partner</th>
                    <th className="px-4 py-3 font-medium">Students</th>
                    <th className="px-4 py-3 font-medium">Updated</th>
                    <th className="px-4 py-3 font-medium">Action</th>
                  </tr>
                </thead>
                <tbody>
                  {submittedBatches.map((batch) => (
                    <tr key={batch.id} className="border-t border-slate-100 bg-white align-top">
                      <td className="px-4 py-3 text-slate-900">
                        <div className="font-semibold">{batch.batchCode}</div>
                        {batch.reviewNote ? <div className="mt-1 max-w-xs text-xs text-slate-500">Last note: {batch.reviewNote}</div> : null}
                      </td>
                      <td className="px-4 py-3 text-slate-700">{batch.institute}</td>
                      <td className="px-4 py-3 text-slate-700">{batch.studentCount}</td>
                      <td className="px-4 py-3 text-slate-700">{batch.updatedAt}</td>
                      <td className="px-4 py-3">
                        <div className="flex gap-2">
                          <button
                            type="button"
                            disabled={workingKey === `approve-batch-${batch.id}`}
                            onClick={() => confirmAndRun(`Approve ${batch.batchCode} for delivery?`, `approve-batch-${batch.id}`, () => approveBatch(batch.id), `${batch.batchCode} approved.`)}
                            className="rounded-lg bg-violet-700 px-3 py-1.5 text-xs font-semibold text-white disabled:opacity-60"
                          >
                            Approve
                          </button>
                          <button
                            type="button"
                            disabled={workingKey === `return-batch-${batch.id}`}
                            onClick={() => promptAndReturnBatch(batch)}
                            className="rounded-lg border border-amber-200 px-3 py-1.5 text-xs font-semibold text-amber-700 disabled:opacity-60"
                          >
                            Return
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                  {submittedBatches.length === 0 && !loading ? (
                    <tr className="border-t border-slate-100 bg-white">
                      <td colSpan={5} className="px-4 py-4 text-center text-slate-500">No submitted batches awaiting review.</td>
                    </tr>
                  ) : null}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Capacity controls</h3>
            <p className="mt-2 text-sm text-slate-600">Set monthly student intake limits for each partner.</p>
            <div className="mt-5 space-y-3 text-sm">
              {capacityControls.map((partner) => (
                <div key={partner.institute} className="flex items-center justify-between rounded-xl border border-slate-200 px-4 py-3">
                  <span className="text-slate-700">{partner.institute}</span>
                  <span className="font-semibold text-slate-900">{partner.seats} seats</span>
                </div>
              ))}
              {capacityControls.length === 0 && !loading ? <div className="rounded-xl border border-slate-200 px-4 py-3 text-slate-500">No capacity records found.</div> : null}
            </div>
          </div>

          <div className="rounded-[2rem] border border-[var(--border-soft)] bg-white/85 p-6 shadow-[var(--shadow-soft)] backdrop-blur">
            <h3 className="font-display text-xl font-semibold tracking-tight text-slate-950">Certificate issue queue</h3>
            <ul className="mt-4 space-y-3 text-sm">
              {certificateQueue.map((entry) => (
                <li key={entry.certificateId} className="rounded-xl border border-slate-200 p-4">
                  <p className="font-semibold text-slate-900">{entry.certificateId}</p>
                  <p className="mt-1 text-slate-600">{entry.student} - {entry.course}</p>
                  <div className="mt-3 flex items-center gap-2">
                    <select
                      value={entry.stage}
                      onChange={(event) => runAction(`certificate-${entry.certificateId}`, () => updateCertificateStage(entry.certificateId, event.target.value), `${entry.certificateId} moved to ${event.target.value}.`)}
                      className="rounded-lg border border-slate-300 px-3 py-2 text-xs font-medium text-slate-700"
                      disabled={workingKey === `certificate-${entry.certificateId}`}
                    >
                      {certificateStages.map((stage) => (
                        <option key={stage} value={stage}>{stage}</option>
                      ))}
                    </select>
                  </div>
                </li>
              ))}
              {certificateQueue.length === 0 && !loading ? <li className="rounded-xl border border-slate-200 p-4 text-slate-500">No certificate queue items.</li> : null}
            </ul>
          </div>
        </div>
      </div>
    </AppShell>
  )
}
